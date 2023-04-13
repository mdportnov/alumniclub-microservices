package ru.mephi.alumniclub.app.controller.handler.impl

import org.springframework.stereotype.Service
import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse
import org.springframework.web.servlet.function.body
import ru.mephi.alumniclub.app.controller.handler.RegistrationLimiterHandler
import ru.mephi.alumniclub.app.model.dto.registration_limiter.RegistrationLimiterSettingsRequest
import ru.mephi.alumniclub.app.service.RegistrationLimiterService
import ru.mephi.alumniclub.shared.dto.common.ApiMessage
import ru.mephi.alumniclub.shared.model.enums.Authority
import ru.mephi.alumniclub.shared.util.extension.assertHasAuthority
import ru.mephi.alumniclub.shared.util.extension.toOkBody
import ru.mephi.alumniclub.shared.util.response.ResponseManager

@Service
class RegistrationLimiterHandlerImpl(
    private val service: RegistrationLimiterService
) : ResponseManager(), RegistrationLimiterHandler {

    override fun setSettings(request: ServerRequest): ServerResponse {
        request.assertHasAuthority(Authority.ADMIN)
        val settings = request.body<RegistrationLimiterSettingsRequest>()
        validate(settings)
        val response = service.setSettings(settings)
        return ApiMessage(message = i18n("label.common.updated"), data = response).toOkBody()
    }

    override fun getSettings(request: ServerRequest): ServerResponse {
        request.assertHasAuthority(Authority.ADMIN)
        val response = service.getSettings()
        return response.toOkBody()
    }
}
