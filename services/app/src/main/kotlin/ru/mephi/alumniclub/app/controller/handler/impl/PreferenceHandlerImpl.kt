package ru.mephi.alumniclub.app.controller.handler.impl

import org.springframework.stereotype.Service
import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse
import org.springframework.web.servlet.function.body
import ru.mephi.alumniclub.app.controller.handler.PreferenceHandler
import ru.mephi.alumniclub.app.model.dto.preferences.UserPreferencesDTO
import ru.mephi.alumniclub.app.service.UserPreferencesService
import ru.mephi.alumniclub.shared.dto.common.ApiMessage
import ru.mephi.alumniclub.shared.dto.security.ScopePermission
import ru.mephi.alumniclub.shared.util.extension.assertHasOneOfPermission
import ru.mephi.alumniclub.shared.util.extension.getPrincipal
import ru.mephi.alumniclub.shared.util.extension.pathVariableOrThrow
import ru.mephi.alumniclub.shared.util.extension.toOkBody
import ru.mephi.alumniclub.shared.util.response.ResponseManager

@Service
class PreferenceHandlerImpl(
    private val service: UserPreferencesService
) : ResponseManager(), PreferenceHandler {

    override fun getPreferences(request: ServerRequest): ServerResponse {
        val userId = request.getPrincipal()
        val response = service.getPreferencesById(userId)
        return response.toOkBody()
    }

    override fun updatePreferences(request: ServerRequest): ServerResponse {
        val userId = request.getPrincipal()
        val updateRequest = request.body<UserPreferencesDTO>()
        val response = service.updatePreferences(userId, updateRequest)
        return ApiMessage(message = i18n("label.common.updated"), response).toOkBody()
    }

    override fun getPreferencesOfUser(request: ServerRequest): ServerResponse {
        request.assertHasOneOfPermission(ScopePermission.USERS)
        val userId = request.pathVariableOrThrow<Long>("id")
        val response = service.getPreferencesById(userId)
        return response.toOkBody()
    }

    override fun turnOffEmailPreference(request: ServerRequest): ServerResponse {
        val token = request.pathVariableOrThrow<String>("token")
        service.updatePreferencesByEmailToken(token)
        return ApiMessage(data = null, message = i18n("label.preference.emailNotificationsDisabled")).toOkBody()
    }
}
