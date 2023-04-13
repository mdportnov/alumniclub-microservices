package ru.mephi.alumniclub.app.service.impl

import org.springframework.data.jpa.repository.Modifying
import org.springframework.stereotype.Service
import ru.mephi.alumniclub.app.database.entity.partnership.Partnership
import ru.mephi.alumniclub.app.database.repository.form.FormJoinPartnershipDao
import ru.mephi.alumniclub.app.database.repository.partnership.PartnershipDao
import ru.mephi.alumniclub.app.model.dto.PageResponse
import ru.mephi.alumniclub.app.model.dto.partnership.request.PartnershipRequest
import ru.mephi.alumniclub.app.model.dto.partnership.response.PartnershipJoinResponse
import ru.mephi.alumniclub.app.model.dto.partnership.response.PartnershipResponse
import ru.mephi.alumniclub.app.model.exception.common.ResourceNotFoundException
import ru.mephi.alumniclub.app.model.mapper.partnership.PartnershipMapper
import ru.mephi.alumniclub.app.service.PartnershipService
import ru.mephi.alumniclub.app.service.StorageManager
import ru.mephi.alumniclub.shared.dto.ExtendedPageRequest
import ru.mephi.alumniclub.shared.model.enums.StoreDir
import ru.mephi.alumniclub.shared.util.response.ResponseManager
import javax.servlet.http.Part
import javax.transaction.Transactional

@Service
@Transactional
class PartnershipServiceImpl(
    private val storageManager: StorageManager,
    private val partnershipDao: PartnershipDao,
    private val formJoinPartnershipDao: FormJoinPartnershipDao,
    private val mapper: PartnershipMapper,
) : ResponseManager(), PartnershipService {
    override fun listForAdmin(pageRequest: ExtendedPageRequest): PageResponse<PartnershipResponse> {
        val partnerships = partnershipDao.findAll(pageRequest.pageable)
        return mapper.asPartnershipPageResponse(partnerships)
    }

    override fun listForPublic(pageRequest: ExtendedPageRequest): PageResponse<PartnershipResponse> {
        val partnerships = partnershipDao.findByCurrentUntilIsAfter(pageRequest.pageable)
        return mapper.asPartnershipPageResponse(partnerships)
    }

    override fun listMembers(id: Long, pageRequest: ExtendedPageRequest): PageResponse<PartnershipJoinResponse> {
        if (!partnershipDao.existsById(id)) throw ResourceNotFoundException()
        val forms = formJoinPartnershipDao.findByPartnershipIdAndStatus(id, pageRequest.pageable)
        return mapper.asMemberPageResponse(forms)
    }

    override fun create(userId: Long, request: PartnershipRequest): PartnershipResponse {
        val partnership = mapper.asEntity(request, userId)
        partnershipDao.save(partnership)
        return mapper.asPartnershipResponse(partnership)
    }

    override fun findById(id: Long): PartnershipResponse {
        val partnership = findEntityById(id)
        return mapper.asPartnershipResponse(partnership)
    }

    override fun findEntityById(id: Long): Partnership {
        return partnershipDao.findById(id).orElseThrow { ResourceNotFoundException() }
    }

    @Modifying
    override fun update(id: Long, request: PartnershipRequest): PartnershipResponse {
        val partnership = findEntityById(id)
        mapper.update(partnership, request)
        return mapper.asPartnershipResponse(partnership)
    }

    @Modifying
    override fun delete(id: Long) {
        val partnership = findEntityById(id)
        partnershipDao.delete(partnership)
    }

    @Modifying
    override fun uploadPhoto(id: Long, file: Part): PartnershipResponse {
        val partnership = findEntityById(id)
        partnership.photoPath = storageManager.saveImage(file, StoreDir.PROJECT, partnership.photoPath, true)
        return mapper.asPartnershipResponse(partnership)
    }
}