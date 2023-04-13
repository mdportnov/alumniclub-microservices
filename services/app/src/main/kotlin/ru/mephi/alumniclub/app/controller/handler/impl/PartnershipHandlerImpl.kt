package ru.mephi.alumniclub.app.controller.handler.impl

import org.springframework.http.HttpStatus
import org.springframework.stereotype.Controller
import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse
import org.springframework.web.servlet.function.body
import ru.mephi.alumniclub.app.controller.handler.PartnershipHandler
import ru.mephi.alumniclub.app.model.dto.partnership.request.PartnershipRequest
import ru.mephi.alumniclub.app.service.PartnershipService
import ru.mephi.alumniclub.shared.dto.common.ApiError
import ru.mephi.alumniclub.shared.dto.common.ApiMessage
import ru.mephi.alumniclub.shared.util.extension.*
import ru.mephi.alumniclub.shared.util.response.ResponseManager

@Controller
class PartnershipHandlerImpl(
    private val service: PartnershipService
) : ResponseManager(), PartnershipHandler {
    override fun findById(request: ServerRequest): ServerResponse {
        val id = request.pathVariableOrThrow<Long>("id")
        return service.findById(id).toOkBody()
    }

    override fun listForAdmin(request: ServerRequest): ServerResponse {
        val page = request.getExtendedPageRequest()
        return service.listForAdmin(page).toOkBody()
    }

    override fun listForPublic(request: ServerRequest): ServerResponse {
        val page = request.getExtendedPageRequest()
        return service.listForPublic(page).toOkBody()
    }

    override fun listMembers(request: ServerRequest): ServerResponse {
        val id = request.pathVariableOrThrow<Long>("id")
        val page = request.getExtendedPageRequest()
        return service.listMembers(id, page).toOkBody()
    }

    override fun create(request: ServerRequest): ServerResponse {
        val userId = request.getPrincipal()
        val partnershipRequest = request.body<PartnershipRequest>()
        validate(partnershipRequest)
        val partnership = service.create(userId, partnershipRequest)
        return ApiMessage(message = i18n("label.common.created"), partnership).toOkBody()
    }

    override fun update(request: ServerRequest): ServerResponse {
        val id = request.pathVariableOrThrow<Long>("id")
        val partnershipRequest = request.body<PartnershipRequest>()
        validate(partnershipRequest)
        val partnership = service.update(id, partnershipRequest)
        return ApiMessage(message = i18n("label.common.updated"), partnership).toOkBody()
    }

    override fun uploadPhoto(request: ServerRequest): ServerResponse {
        val id = request.pathVariableOrThrow<Long>("id")
        val file = request.getMultiPartPhoto()
            ?: throw ApiError(HttpStatus.BAD_REQUEST, i18n("exception.media.invalidMultipart"))
        val partnership = service.uploadPhoto(id, file)
        return ApiMessage(message = i18n("label.common.updated"), partnership).toOkBody()
    }

    override fun delete(request: ServerRequest): ServerResponse {
        val id = request.pathVariableOrThrow<Long>("id")
        service.delete(id)
        return ApiMessage(data = null, message = i18n("label.common.deleted")).toOkBody()
    }
}