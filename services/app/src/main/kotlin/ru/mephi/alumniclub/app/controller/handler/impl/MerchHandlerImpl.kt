package ru.mephi.alumniclub.app.controller.handler.impl

import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse
import org.springframework.web.servlet.function.ServerResponse.created
import org.springframework.web.servlet.function.body
import ru.mephi.alumniclub.app.controller.handler.MerchHandler
import ru.mephi.alumniclub.app.model.dto.atom.request.MerchRequest
import ru.mephi.alumniclub.app.service.MerchService
import ru.mephi.alumniclub.shared.dto.common.ApiError
import ru.mephi.alumniclub.shared.dto.common.ApiMessage
import ru.mephi.alumniclub.shared.dto.security.ScopePermission
import ru.mephi.alumniclub.shared.util.constants.API_VERSION_1
import ru.mephi.alumniclub.shared.util.constants.SERVER_NAME
import ru.mephi.alumniclub.shared.util.extension.assertHasOneOfPermission
import ru.mephi.alumniclub.shared.util.extension.getMultiPartPhoto
import ru.mephi.alumniclub.shared.util.extension.toOkBody
import ru.mephi.alumniclub.shared.util.response.ResponseManager
import java.net.URI

@Component
class MerchHandlerImpl(
    private val service: MerchService
) : ResponseManager(), MerchHandler {
    override fun getAllAvailableMerch(request: ServerRequest): ServerResponse {
        return service.getAllAvailableMerch().toOkBody()
    }

    override fun getAllMerch(request: ServerRequest): ServerResponse {
        request.assertHasOneOfPermission(ScopePermission.ATOMS)
        return service.getAllMerch().toOkBody()
    }

    override fun getMerchById(request: ServerRequest): ServerResponse {
        request.assertHasOneOfPermission(ScopePermission.ATOMS)
        val merchId = request.pathVariable("id").toLong()
        return service.getMerchById(merchId).toOkBody()
    }

    override fun create(request: ServerRequest): ServerResponse {
        request.assertHasOneOfPermission(ScopePermission.ATOMS)
        val createMerchRequest = request.body<MerchRequest>()
        validate(createMerchRequest)
        val merch = service.createMerch(createMerchRequest)
        val message = ApiMessage(message = i18n("label.common.created"), merch)
        return created(URI("$SERVER_NAME$API_VERSION_1/admin/merch/${merch.id}")).body(message)
    }

    override fun uploadPhoto(request: ServerRequest): ServerResponse {
        request.assertHasOneOfPermission(ScopePermission.ATOMS)
        val file = request.getMultiPartPhoto()
            ?: throw ApiError(HttpStatus.BAD_REQUEST, i18n("exception.media.invalidMultipart"))
        val merchId = request.pathVariable("id").toLong()
        val merch = service.uploadPhoto(merchId, file)
        return ApiMessage(message = i18n("label.common.updated"), merch).toOkBody()
    }

    override fun updateMerch(request: ServerRequest): ServerResponse {
        request.assertHasOneOfPermission(ScopePermission.ATOMS)
        val updateMerchRequest = request.body<MerchRequest>()
        validate(updateMerchRequest)
        val merchId = request.pathVariable("id").toLong()
        val updatedMerch = service.updateMerch(merchId, updateMerchRequest)
        return ApiMessage(message = i18n("label.common.updated"), updatedMerch).toOkBody()
    }

    override fun deleteMerch(request: ServerRequest): ServerResponse {
        request.assertHasOneOfPermission(ScopePermission.ATOMS)
        val merchId = request.pathVariable("id").toLong()
        service.deleteMerch(merchId)
        return ApiMessage(data = null, message = i18n("label.common.deleted")).toOkBody()
    }
}
