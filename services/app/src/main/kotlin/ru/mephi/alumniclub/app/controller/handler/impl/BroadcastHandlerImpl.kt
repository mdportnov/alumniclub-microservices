package ru.mephi.alumniclub.app.controller.handler.impl

import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse
import org.springframework.web.servlet.function.body
import ru.mephi.alumniclub.app.controller.handler.BroadcastHandler
import ru.mephi.alumniclub.app.model.dto.broadcast.request.BroadcastNotificationRequest
import ru.mephi.alumniclub.app.model.dto.broadcast.request.BroadcastRequest
import ru.mephi.alumniclub.app.service.BroadcastService
import ru.mephi.alumniclub.shared.dto.common.ApiError
import ru.mephi.alumniclub.shared.dto.common.ApiMessage
import ru.mephi.alumniclub.shared.dto.security.ScopePermission
import ru.mephi.alumniclub.shared.util.extension.*
import ru.mephi.alumniclub.shared.util.response.ResponseManager
import java.util.*

@Service
class BroadcastHandlerImpl(
    private val broadcastService: BroadcastService
) : ResponseManager(), BroadcastHandler {

    override fun createBroadcast(request: ServerRequest): ServerResponse {
        request.assertHasOneOfPermission(ScopePermission.BROADCASTS)
        val userId = request.getPrincipal()
        val broadcast = request.body<BroadcastRequest>()
        validate(request)
        val response = broadcastService.create(userId, broadcast)
        return ApiMessage(message = i18n("label.common.created"), response).toOkBody()
    }

    override fun delete(request: ServerRequest): ServerResponse {
        request.assertHasOneOfPermission(ScopePermission.BROADCASTS)
        val id = request.pathVariableOrThrow<UUID>("id")
        broadcastService.delete(id)
        return ApiMessage(data = null, message = i18n("label.common.deleted")).toOkBody()
    }

    override fun update(request: ServerRequest): ServerResponse {
        request.assertHasOneOfPermission(ScopePermission.BROADCASTS)
        val id = request.pathVariableOrThrow<UUID>("id")
        val body = request.body<BroadcastNotificationRequest>()
        validate(request)
        val response = broadcastService.update(id, body)
        return ApiMessage(message = i18n("label.common.updated"), response).toOkBody()
    }

    override fun uploadPhoto(request: ServerRequest): ServerResponse {
        request.assertHasOneOfPermission(ScopePermission.BROADCASTS)
        val id = request.pathVariableOrThrow<UUID>("id")
        val file = request.getMultiPartPhoto()
            ?: throw ApiError(HttpStatus.BAD_REQUEST, i18n("exception.media.invalidMultipart"))
        val response = broadcastService.uploadPhoto(id, file)
        return ApiMessage(message = i18n("label.common.updated"), response).toOkBody()
    }

    override fun getShortById(request: ServerRequest): ServerResponse {
        val id = request.pathVariableOrThrow<UUID>("id")
        val response = broadcastService.getShortById(id)
        return response.toOkBody()
    }

    override fun getById(request: ServerRequest): ServerResponse {
        request.assertHasOneOfPermission(ScopePermission.BROADCASTS)
        val id = request.pathVariableOrThrow<UUID>("id")
        val response = broadcastService.getById(id)
        return response.toOkBody()
    }
}
