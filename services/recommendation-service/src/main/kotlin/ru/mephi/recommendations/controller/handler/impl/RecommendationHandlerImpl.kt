package ru.mephi.recommendations.controller.handler.impl

import org.springframework.stereotype.Service
import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse
import ru.mephi.alumniclub.shared.util.extension.paramOrThrow
import ru.mephi.alumniclub.shared.util.extension.pathVariableOrThrow
import ru.mephi.recommendations.controller.handler.RecommendationHandler
import ru.mephi.recommendations.service.RecommendationService
import java.util.*

@Service
class RecommendationHandlerImpl(
    private val service: RecommendationService,
) : RecommendationHandler {
    override fun getRecommendationByPublicationId(request: ServerRequest): ServerResponse {
        val id = request.pathVariableOrThrow<UUID>("id")
        val count = request.paramOrThrow<Int>("count")
        val response = service.getRecommendationByPublication(id, count)
        return ServerResponse.ok().body(response)
    }
}