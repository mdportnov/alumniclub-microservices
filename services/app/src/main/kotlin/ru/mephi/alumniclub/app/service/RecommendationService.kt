package ru.mephi.alumniclub.app.service

import ru.mephi.alumniclub.app.model.dto.recommendations.ContentBasedRecommendationResponse
import java.util.*

interface RecommendationService {
    fun getContentBasedRecommendation(id: UUID, userId: Long): ContentBasedRecommendationResponse
    fun recalculateAllPublicationVectors()
}