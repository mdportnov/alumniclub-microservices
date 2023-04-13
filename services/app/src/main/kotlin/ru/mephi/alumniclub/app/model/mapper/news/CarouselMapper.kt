package ru.mephi.alumniclub.app.model.mapper.news

import org.springframework.stereotype.Component
import ru.mephi.alumniclub.app.database.entity.news.Carousel
import ru.mephi.alumniclub.app.model.dto.news.CarouselRequest
import ru.mephi.alumniclub.app.model.dto.news.CarouselResponse

@Component
class CarouselMapper {
    fun asEntity(request: CarouselRequest): Carousel {
        return Carousel(
            title = request.title,
            link = request.link,
            photoPath = null
        )
    }

    fun update(entity: Carousel, request: CarouselRequest) {
        entity.title = request.title
        entity.link = request.link
    }

    fun asResponse(entity: Carousel): CarouselResponse {
        return CarouselResponse(
            id = entity.id,
            createdAt = entity.createdAt,
            title = entity.title,
            link = entity.link,
            photoPath = entity.photoPath
        )
    }

    fun asListResponses(list: List<Carousel>) = list.map(::asResponse)
}
