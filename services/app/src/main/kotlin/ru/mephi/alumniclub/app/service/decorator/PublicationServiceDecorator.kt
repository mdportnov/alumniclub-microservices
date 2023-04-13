package ru.mephi.alumniclub.app.service.decorator

import org.springframework.context.annotation.Lazy
import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Service
import ru.mephi.alumniclub.app.model.dto.feed.request.PublicationRequest
import ru.mephi.alumniclub.app.model.dto.feed.request.PublicationUpdateRequest
import ru.mephi.alumniclub.app.model.dto.feed.response.admin.PublicationResponseForAdmin
import ru.mephi.alumniclub.app.model.dto.feed.response.user.PublicationResponseForUser
import ru.mephi.alumniclub.app.model.dto.publication.AbstractPublicationEventRequest
import ru.mephi.alumniclub.app.model.mapper.feed.PublicationMapper
import ru.mephi.alumniclub.app.service.PublicationService
import ru.mephi.alumniclub.app.service.RabbitService
import ru.mephi.alumniclub.app.service.RecommendationService
import ru.mephi.alumniclub.app.service.impl.publication.PublicationServiceImpl
import ru.mephi.alumniclub.shared.util.enums.RabbitMessageType
import java.util.*

@Primary
@Service
class PublicationServiceDecorator(
    @Lazy
    private val recommendationService: RecommendationService,
    private val service: PublicationServiceImpl,
    private val publicationMapper: PublicationMapper,
    private val rabbitService: RabbitService
) : PublicationService by service {
    override fun deletePublication(feedId: Long, publicationId: UUID) {
        val response = service.deletePublication(feedId, publicationId)
        rabbitService.sendMessage(
            AbstractPublicationEventRequest.PublicationDeletedEventRequest(publicationId),
            RabbitMessageType.PUBLICATIONS_EVENT_QUEUE
        )
        return response
    }

    override fun post(userId: Long, feedId: Long, request: PublicationRequest): PublicationResponseForAdmin {
        val response = service.post(userId, feedId, request)
        rabbitService.sendMessage(
            AbstractPublicationEventRequest.PublicationCreatedEventRequest(
                response.id, response.title, request.content
            ),
            RabbitMessageType.PUBLICATIONS_EVENT_QUEUE
        )
        return response
    }

    override fun update(
        feedId: Long,
        publicationId: UUID,
        request: PublicationUpdateRequest,
    ): PublicationResponseForAdmin {
        val response = service.update(feedId, publicationId, request)
        rabbitService.sendMessage(
            AbstractPublicationEventRequest.PublicationUpdatedEventRequest(
                response.id, response.title, request.content
            ),
            RabbitMessageType.PUBLICATIONS_EVENT_QUEUE
        )
        return response
    }

    override fun findPublicationByIdForUser(
        userId: Long,
        feedId: Long,
        publicationId: UUID,
        withRecommendation: Boolean
    ): PublicationResponseForUser {
        val response = service.findPublicationByIdForUser(userId, feedId, publicationId, withRecommendation)
        if (!withRecommendation) return response
        val recommendation = recommendationService.getContentBasedRecommendation(publicationId, userId)
        return publicationMapper.asResponseForUser(response, recommendation)
    }
}