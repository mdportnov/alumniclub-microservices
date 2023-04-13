package ru.mephi.alumniclub.app.controller.handler.impl

import org.springframework.stereotype.Service
import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse
import org.springframework.web.servlet.function.ServerResponse.ok
import org.springframework.web.servlet.function.body
import ru.mephi.alumniclub.app.controller.handler.FcmTokenHandler
import ru.mephi.alumniclub.app.model.dto.fcm.request.RemoveFcmTokenRequest
import ru.mephi.alumniclub.app.model.dto.fcm.request.UploadTokenRequest
import ru.mephi.alumniclub.app.service.FcmTokenService
import ru.mephi.alumniclub.shared.util.extension.getPrincipal
import ru.mephi.alumniclub.shared.util.response.ResponseManager

@Service
class FcmTokenHandlerImpl(
    private val service: FcmTokenService
) : ResponseManager(), FcmTokenHandler {

    override fun uploadToken(request: ServerRequest): ServerResponse {
        val userId = request.getPrincipal()
        val tokenRequest = request.body<UploadTokenRequest>()
        validate(tokenRequest)
        service.uploadToken(tokenRequest, userId)
        return ok().build()
    }

    override fun removeToken(request: ServerRequest): ServerResponse {
        val userId = request.getPrincipal()
        val tokenRequest = request.body<RemoveFcmTokenRequest>()
        validate(tokenRequest)
        service.removeToken(tokenRequest, userId)
        return ok().build()
    }

}