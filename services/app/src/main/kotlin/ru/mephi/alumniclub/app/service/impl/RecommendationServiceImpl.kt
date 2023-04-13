package ru.mephi.alumniclub.app.service.impl

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Lazy
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.getForEntity
import ru.mephi.alumniclub.app.database.entity.publication.Publication
import ru.mephi.alumniclub.app.model.dto.publication.AbstractPublicationEventRequest
import ru.mephi.alumniclub.app.model.dto.recommendations.ContentBasedRecommendationResponse
import ru.mephi.alumniclub.app.model.exception.common.ResourceNotFoundException
import ru.mephi.alumniclub.app.model.mapper.feed.PublicationMapper
import ru.mephi.alumniclub.app.service.*
import ru.mephi.alumniclub.shared.dto.recommendations.RecommendationResponse
import ru.mephi.alumniclub.shared.util.enums.RabbitMessageType
import ru.mephi.alumniclub.shared.util.response.ResponseManager
import java.time.LocalDateTime
import java.util.*

@Service
class RecommendationServiceImpl(
    @Lazy private val publicationService: PublicationService,
    private val httpTemplate: RestTemplate,
    private val likeService: LikeService,
    private val projectService: ProjectService,
    private val publicationMapper: PublicationMapper,
    private val rabbitService: RabbitService,
    @Value("\${recommendation-service.host}") private val recommendationHost: String,
    @Value("\${recommendation-service.port}") private val recommendationPort: Int
) : ResponseManager(), RecommendationService {

    private val url = "http://$recommendationHost:$recommendationPort/recommendations/api/v1/publication"

    private fun getRecommendationByPublication(publicationId: UUID, count: Int = 5): RecommendationResponse? {
        return try {
            httpTemplate.getForEntity<RecommendationResponse>("$url/$publicationId?count=$count").body
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    override fun getContentBasedRecommendation(id: UUID, userId: Long): ContentBasedRecommendationResponse {
        val publication = publicationService.findPublicationEntity(id)
        val response = getRecommendationByPublication(publication.id)
        val page = if (response == null) Page.empty() else findPublicationsByIds(response.ids)
        val recommendation = publicationMapper.asPageResponseForUser(userId, page)
        return ContentBasedRecommendationResponse(recommendation)
    }

    override fun recalculateAllPublicationVectors() {
        val total = publicationService.findAllPublications(PageRequest.of(0, 10)).totalPages
        for (i in 0 until total) {
            publicationService.findAllPublications(PageRequest.of(i, 10)).forEach {
                rabbitService.sendMessage(
                    AbstractPublicationEventRequest.PublicationUpdatedEventRequest(it.id, it.title, it.content),
                    RabbitMessageType.PUBLICATIONS_EVENT_QUEUE
                )
            }
        }
    }

    private fun sendDeleteRecommendationRequest(id: UUID) {
        rabbitService.sendMessage(
            AbstractPublicationEventRequest.PublicationDeletedEventRequest(id),
            RabbitMessageType.PUBLICATIONS_EVENT_QUEUE
        )
    }

    private fun findPublicationsByIds(ids: List<UUID>): Page<Publication> {
        val publications = ids.mapNotNull { id ->
            try {
                publicationService.findPublicationEntity(id, true)
            } catch (e: ResourceNotFoundException) {
                sendDeleteRecommendationRequest(id)
                null
            }
        }.filter { !it.hidden && it.publicationDate <= LocalDateTime.now() }
        return PageImpl(publications.take(3))
    }
}