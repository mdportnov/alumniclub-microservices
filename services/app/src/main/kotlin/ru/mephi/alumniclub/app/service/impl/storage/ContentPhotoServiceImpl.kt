package ru.mephi.alumniclub.app.service.impl.storage

import org.springframework.stereotype.Service
import ru.mephi.alumniclub.app.database.entity.content.ContentPhoto
import ru.mephi.alumniclub.app.database.repository.content.ContentPhotoDao
import ru.mephi.alumniclub.app.model.dto.content.ContentPhotoResponse
import ru.mephi.alumniclub.app.service.ContentPhotoService
import ru.mephi.alumniclub.app.service.RabbitService
import ru.mephi.alumniclub.app.service.StorageManager
import ru.mephi.alumniclub.shared.dto.photo.ContentPhotoRequest
import ru.mephi.alumniclub.shared.model.enums.StoreDir
import ru.mephi.alumniclub.shared.util.enums.RabbitMessageType
import javax.servlet.http.Part
import javax.transaction.Transactional

@Service
class ContentPhotoServiceImpl(
    private val rabbitService: RabbitService,
    private val storageManager: StorageManager,
    private val contentPhotoDao: ContentPhotoDao,
) : ContentPhotoService {

    override fun queueContent(request: ContentPhotoRequest) {
        rabbitService.sendMessage(request, RabbitMessageType.CONTENT_PHOTO_QUEUE)
    }

    private fun findByPhotoPath(photoPath: String) =
        contentPhotoDao.findByPhotoPath(photoPath)

    override fun contentExists(photoPath: String): Boolean {
        val photo = contentPhotoDao.findByPhotoPath(photoPath) ?: return false
        return photo.content != null
    }

    override fun createContentPhoto(file: Part): ContentPhotoResponse {
        val path = storageManager.saveImage(file, StoreDir.CONTENT_PHOTO, null, true)
        contentPhotoDao.save(ContentPhoto(path))
        return ContentPhotoResponse(storageManager.getPhotoPath(path))
    }

    @Transactional
    override fun deleteContentPhoto(photoPath: String) {
        val contentPhoto = findByPhotoPath(photoPath) ?: return
        contentPhotoDao.delete(contentPhoto)
        storageManager.removeImage(photoPath, StoreDir.CONTENT_PHOTO)
    }
}