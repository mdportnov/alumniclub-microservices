package ru.mephi.recommendations.service.impl

import org.springframework.stereotype.Service
import ru.mephi.alumniclub.shared.dto.recommendations.RecommendationResponse
import ru.mephi.alumniclub.shared.logging.AlumniLogger
import ru.mephi.recommendations.database.milvus.publication.Publication
import ru.mephi.recommendations.service.PublicationService
import ru.mephi.recommendations.service.RecommendationService
import java.util.*
import java.util.logging.Level
import java.util.logging.Logger

@Service
class RecommendationServiceImpl(
    private val publicationService: PublicationService,
    private val logger: AlumniLogger
) : RecommendationService {

    /**
     * Gets recommendations for entered publication
     *
     * @param publicationId id of the [Publication] for which recommendations should be given
     * @param count of [Publication]s that had will be recommended
     * @return [RecommendationResponse]
     */
    override fun getRecommendationByPublication(publicationId: UUID, count: Int): RecommendationResponse {
        logger.info("Gets recommendation to [$publicationId]")
        val list = publicationService.getNearestPublicationsIds(publicationId, count)
        logger.info("Recommendations to [$publicationId] is [$list]")
        return RecommendationResponse(list)
    }
}