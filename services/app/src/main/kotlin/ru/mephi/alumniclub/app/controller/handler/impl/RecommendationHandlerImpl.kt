package ru.mephi.alumniclub.app.controller.handler.impl

import org.springframework.stereotype.Component
import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse
import ru.mephi.alumniclub.app.controller.handler.RecommendationHandler
import ru.mephi.alumniclub.app.service.RecommendationService
import ru.mephi.alumniclub.shared.dto.security.ScopePermission
import ru.mephi.alumniclub.shared.util.extension.assertHasOneOfPermission
import ru.mephi.alumniclub.shared.util.extension.getPrincipal
import ru.mephi.alumniclub.shared.util.extension.pathVariableOrThrow
import ru.mephi.alumniclub.shared.util.extension.toOkBody
import java.util.*

@Component
class RecommendationHandlerImpl(
    private val service: RecommendationService
) : RecommendationHandler {

    override fun getContentBasedRecommendation(request: ServerRequest): ServerResponse {
        val id = request.pathVariableOrThrow<UUID>("id")
        val userId = request.getPrincipal()
        val response = service.getContentBasedRecommendation(id, userId)
        return response.toOkBody()
    }

    override fun recalculateAllPublicationVectors(request: ServerRequest): ServerResponse {
        request.assertHasOneOfPermission(ScopePermission.FEEDS_MODIFY)
        service.recalculateAllPublicationVectors()
        return ServerResponse.ok().build()
    }
}