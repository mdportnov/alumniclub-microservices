package ru.mephi.recommendations.controller.handler

import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse

interface RecommendationHandler {
    fun getRecommendationByPublicationId(request: ServerRequest): ServerResponse
}