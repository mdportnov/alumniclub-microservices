package ru.mephi.alumniclub.app.service.impl.broadcast

import org.springframework.data.jpa.repository.Modifying
import org.springframework.stereotype.Service
import ru.mephi.alumniclub.app.database.entity.broadcast.Broadcast
import ru.mephi.alumniclub.app.database.repository.broadcast.BroadcastNotificationRepository
import ru.mephi.alumniclub.app.model.dto.broadcast.request.BroadcastNotificationRequest
import ru.mephi.alumniclub.app.model.dto.broadcast.request.BroadcastRequest
import ru.mephi.alumniclub.app.model.dto.broadcast.response.BroadcastNotificationResponse
import ru.mephi.alumniclub.app.model.dto.broadcast.response.BroadcastNotificationShortResponse
import ru.mephi.alumniclub.app.model.exception.common.ResourceNotFoundException
import ru.mephi.alumniclub.app.model.mapper.broadcast.BroadcastNotificationMapper
import ru.mephi.alumniclub.app.model.mapper.broadcast.BroadcastSenderMapper
import ru.mephi.alumniclub.app.service.BroadcastSenderService
import ru.mephi.alumniclub.app.service.BroadcastService
import ru.mephi.alumniclub.app.service.ContentPhotoService
import ru.mephi.alumniclub.app.service.StorageManager
import ru.mephi.alumniclub.shared.dto.photo.ContentPhotoRequest
import ru.mephi.alumniclub.shared.model.enums.StoreDir
import ru.mephi.alumniclub.shared.util.response.ResponseManager
import java.util.*
import javax.servlet.http.Part
import javax.transaction.Transactional
import kotlin.jvm.optionals.getOrNull

@Service
class BroadcastServiceImpl(
    private val dao: BroadcastNotificationRepository,
    private val storageManager: StorageManager,
    private val broadcastSender: BroadcastSenderService,
    private val contentPhotoService: ContentPhotoService,
    private val mapper: BroadcastNotificationMapper,
    private val broadcastSenderMapper: BroadcastSenderMapper
) : ResponseManager(), BroadcastService {

    override fun findById(id: UUID): Broadcast {
        return dao.findById(id).orElseThrow {
            ResourceNotFoundException(Broadcast::class.java, id)
        }
    }

    @OptIn(ExperimentalStdlibApi::class)
    override fun findEntityOrNullById(id: UUID): Broadcast? {
        return dao.findById(id).getOrNull()
    }

    override fun getById(id: UUID): BroadcastNotificationResponse {
        return mapper.asResponse(findById(id))
    }

    override fun getShortById(id: UUID): BroadcastNotificationShortResponse {
        val entity = findById(id)
        return mapper.asShortResponse(entity)
    }

    @Modifying
    @Transactional
    override fun create(userId: Long, request: BroadcastRequest): BroadcastNotificationResponse {
        val entity = mapper.asEntity(request, userId)
        dao.save(entity)
        contentPhotoService.queueContent(ContentPhotoRequest(entity.id, entity.content))
        broadcastSender.createBroadcast(userId, broadcastSenderMapper.asBroadcastByPublication(entity, request))
        return mapper.asResponse(entity)
    }

    @Modifying
    @Transactional
    override fun update(id: UUID, request: BroadcastNotificationRequest): BroadcastNotificationResponse {
        val entity = findById(id)
        mapper.update(entity, request)
        contentPhotoService.queueContent(ContentPhotoRequest(entity.id, entity.content))
        return mapper.asResponse(entity)
    }

    @Modifying
    @Transactional
    override fun uploadPhoto(id: UUID, file: Part): BroadcastNotificationResponse {
        val entity = findById(id)
        entity.photoPath = storageManager.saveImage(file, StoreDir.BROADCAST, entity.photoPath, true)
        return mapper.asResponse(entity)
    }

    @Modifying
    @Transactional
    override fun delete(id: UUID) {
        val entity = findById(id)
        dao.delete(entity)
        storageManager.removeImage(entity.photoPath, StoreDir.BROADCAST)
    }
}
