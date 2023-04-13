package ru.mephi.alumniclub.app.controller.handler

import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse

interface RecommendationHandler {
    fun getContentBasedRecommendation(request: ServerRequest): ServerResponse
    fun recalculateAllPublicationVectors(request: ServerRequest): ServerResponse
}