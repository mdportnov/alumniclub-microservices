package ru.mephi.recommendations.service

import ru.mephi.alumniclub.shared.dto.recommendations.RecommendationResponse
import java.util.*

interface RecommendationService {
    fun getRecommendationByPublication(publicationId: UUID, count: Int = 3): RecommendationResponse
}