package ru.mephi.alumniclub.app.controller.handler.impl

import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse
import org.springframework.web.servlet.function.ServerResponse.ok
import ru.mephi.alumniclub.app.controller.handler.NotificationHandler
import ru.mephi.alumniclub.app.service.NotificationService
import ru.mephi.alumniclub.shared.dto.common.ApiMessage
import ru.mephi.alumniclub.shared.util.extension.getCursorRequest
import ru.mephi.alumniclub.shared.util.extension.getExtendedPageRequest
import ru.mephi.alumniclub.shared.util.extension.getPrincipal
import ru.mephi.alumniclub.shared.util.extension.toOkBody
import ru.mephi.alumniclub.shared.util.response.ResponseManager

@Service
class NotificationHandlerImpl(
    private val service: NotificationService
) : ResponseManager(), NotificationHandler {

    override fun listForUser(request: ServerRequest): ServerResponse {
        val userId = request.getPrincipal()
        val cursor = request.getCursorRequest()
        val response = service.list(userId, cursor)
        return response.toOkBody()
    }

    override fun listForAdmin(request: ServerRequest): ServerResponse {
        val page = request.getExtendedPageRequest()
        val response = service.list(page)
        return response.toOkBody()
    }

    override fun findByIdForUser(request: ServerRequest): ServerResponse {
        val id = request.pathVariable("id").toLong()
        val notification = service.getByIdForUser(request.getPrincipal(), id)
        return ok().contentType(MediaType.APPLICATION_JSON).body(notification)
    }

    override fun findByIdForAdmin(request: ServerRequest): ServerResponse {
        val id = request.pathVariable("id").toLong()
        val notification = service.getByIdForAdmin(id)
        return ok().contentType(MediaType.APPLICATION_JSON).body(notification)
    }

    override fun getUserNotificationsInfo(request: ServerRequest): ServerResponse {
        val userId = request.getPrincipal()
        val response = service.getCountOfUnreadNotifications(userId)
        return response.toOkBody()
    }

    override fun deleteForUser(request: ServerRequest): ServerResponse {
        val userId = request.getPrincipal()
        val notificationId = request.pathVariable("id").toLong()
        service.deleteForUser(userId, notificationId)
        return ApiMessage(data = null, message = i18n("label.common.deleted")).toOkBody()
    }

    override fun delete(request: ServerRequest): ServerResponse {
        val notificationId = request.pathVariable("id").toLong()
        service.delete(notificationId)
        return ApiMessage(data = null, message = i18n("label.common.deleted")).toOkBody()
    }
}
