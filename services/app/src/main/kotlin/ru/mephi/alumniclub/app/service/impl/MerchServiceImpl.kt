package ru.mephi.alumniclub.app.service.impl

import org.springframework.stereotype.Service
import ru.mephi.alumniclub.app.database.entity.atom.Merch
import ru.mephi.alumniclub.app.database.repository.MerchDao
import ru.mephi.alumniclub.app.model.dto.atom.request.MerchRequest
import ru.mephi.alumniclub.app.model.dto.atom.response.MerchResponse
import ru.mephi.alumniclub.app.model.exception.common.ResourceNotFoundException
import ru.mephi.alumniclub.app.model.mapper.atom.MerchMapper
import ru.mephi.alumniclub.app.service.MerchService
import ru.mephi.alumniclub.app.service.StorageManager
import ru.mephi.alumniclub.shared.model.enums.StoreDir
import ru.mephi.alumniclub.shared.util.response.ResponseManager
import javax.servlet.http.Part
import javax.transaction.Transactional

@Service
class MerchServiceImpl(
    private val storageManager: StorageManager,
    private val merchDao: MerchDao,
    private val merchMapper: MerchMapper
) : ResponseManager(), MerchService {
    override fun findMerchEntityById(id: Long): Merch {
        return merchDao.findById(id).orElseThrow { ResourceNotFoundException(Merch::class.java, id) }
    }

    override fun getAllAvailableMerch(): List<MerchResponse> {
        return merchMapper.asResponseList(merchDao.findAllByAvailabilityIsTrue())
    }

    override fun getAllMerch(): List<MerchResponse> {
        return merchMapper.asResponseList(merchDao.findAll().toList())
    }

    override fun getMerchById(id: Long): MerchResponse {
        return merchMapper.asResponse(findMerchEntityById(id))
    }

    @Transactional
    override fun updateMerch(id: Long, request: MerchRequest): MerchResponse {
        val merch = findMerchEntityById(id)
        return merchMapper.update(merch, request).let { merchMapper.asResponse(it) }
    }

    override fun deleteMerch(id: Long) {
        val merch = findMerchEntityById(id)
        merchDao.deleteMerchById(id)
        storageManager.removeImage(merch.photoPath, StoreDir.MERCH)
    }

    override fun createMerch(request: MerchRequest): MerchResponse {
        val merch = merchDao.save(merchMapper.asEntity(request))
        return merchMapper.asResponse(merch)
    }

    override fun uploadPhoto(id: Long, file: Part): MerchResponse {
        val merch = merchDao.findById(id).orElseThrow {
            ResourceNotFoundException(Merch::class.java, id)
        }
        merch.photoPath = storageManager.saveImage(file, StoreDir.MERCH, merch.photoPath, true)
        return merchMapper.asResponse(merch)
    }
}
