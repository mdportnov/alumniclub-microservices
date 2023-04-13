package ru.mephi.alumniclub.app.service.impl

import org.springframework.data.jpa.repository.Modifying
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import ru.mephi.alumniclub.app.database.entity.news.Carousel
import ru.mephi.alumniclub.app.database.repository.news.CarouselDao
import ru.mephi.alumniclub.app.model.dto.news.CarouselRequest
import ru.mephi.alumniclub.app.model.dto.news.CarouselResponse
import ru.mephi.alumniclub.app.model.exception.common.ResourceNotFoundException
import ru.mephi.alumniclub.app.model.mapper.news.CarouselMapper
import ru.mephi.alumniclub.app.service.CarouselService
import ru.mephi.alumniclub.app.service.StorageManager
import ru.mephi.alumniclub.shared.dto.common.ApiError
import ru.mephi.alumniclub.shared.model.enums.StoreDir
import ru.mephi.alumniclub.shared.util.response.ResponseManager
import javax.servlet.http.Part
import javax.transaction.Transactional

@Service
class CarouselServiceImpl(
    private val dao: CarouselDao,
    private val mapper: CarouselMapper,
    private val storageManager: StorageManager,
) : ResponseManager(), CarouselService {
    override fun findEntityById(id: Long): Carousel {
        return dao.findById(id).orElseThrow { ResourceNotFoundException(Carousel::class.java, id) }
    }

    override fun findAll(): List<Carousel> {
        return dao.findAll().toList()
    }

    @Transactional
    @Modifying
    override fun uploadPhoto(id: Long, file: Part): CarouselResponse {
        val news = findEntityById(id)
        news.photoPath = storageManager.saveImage(file, StoreDir.CAROUSEL, news.photoPath, true)
        return mapper.asResponse(news)
    }

    @Transactional
    override fun deleteById(id: Long) {
        val news = findEntityById(id)
        dao.delete(news)
        storageManager.removeImage(news.photoPath, StoreDir.MERCH)
    }

    @Transactional
    override fun create(request: CarouselRequest): CarouselResponse {
        if (dao.count() >= 10)
            throw ApiError(HttpStatus.BAD_REQUEST, i18n("exception.alreadyExists.fullCarousel", "10"))
        val entity = mapper.asEntity(request)
        dao.save(entity)
        return mapper.asResponse(entity)
    }

    @Transactional
    @Modifying
    override fun update(id: Long, request: CarouselRequest): CarouselResponse {
        val entity = findEntityById(id)
        mapper.update(entity, request)
        return mapper.asResponse(entity)
    }

    override fun getById(id: Long): CarouselResponse {
        val news = findEntityById(id)
        return mapper.asResponse(news)
    }

    override fun getAll(): List<CarouselResponse> {
        return mapper.asListResponses(findAll())
    }
}
