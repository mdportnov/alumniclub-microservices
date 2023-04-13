package ru.mephi.alumniclub.app.controller.handler.impl

import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse
import org.springframework.web.servlet.function.body
import ru.mephi.alumniclub.app.controller.handler.UserHandler
import ru.mephi.alumniclub.app.model.dto.auth.password.request.RefreshPasswordRequest
import ru.mephi.alumniclub.app.model.dto.user.UserVisibilityDTO
import ru.mephi.alumniclub.app.model.dto.user.request.BanRequest
import ru.mephi.alumniclub.app.model.dto.user.request.BioRequest
import ru.mephi.alumniclub.app.model.dto.user.request.DevicesRequest
import ru.mephi.alumniclub.app.model.dto.user.request.UpdateUserRequest
import ru.mephi.alumniclub.app.service.AuthService
import ru.mephi.alumniclub.app.service.UserService
import ru.mephi.alumniclub.app.util.RefreshCookieGenerator
import ru.mephi.alumniclub.shared.dto.common.ApiError
import ru.mephi.alumniclub.shared.dto.common.ApiMessage
import ru.mephi.alumniclub.shared.dto.security.ScopePermission
import ru.mephi.alumniclub.shared.util.extension.*
import ru.mephi.alumniclub.shared.util.response.ResponseManager


@Component
class UserHandlerImpl(
    private val userService: UserService,
    private val authService: AuthService,
    private val refreshCookieGenerator: RefreshCookieGenerator
) : ResponseManager(), UserHandler {
    override fun listForAdmin(request: ServerRequest): ServerResponse {
        request.assertHasOneOfPermission(ScopePermission.USERS)
        val page = request.getExtendedPageRequest()
        val query = request.paramOrElse("query") { "" }.trim()
        val users = userService.list(query, page)
        return users.toOkBody()
    }

    override fun findByIdForAdmin(request: ServerRequest): ServerResponse {
        request.assertHasOneOfPermission(ScopePermission.USERS)
        val id = request.pathVariableOrThrow<Long>("id")
        val user = userService.getUserById(id, visibilityCheck = false)
        return user.toOkBody()
    }

    override fun findProfileByIdForAdmin(request: ServerRequest): ServerResponse {
        request.assertHasOneOfPermission(ScopePermission.USERS)
        val userId = request.pathVariableOrThrow<Long>("id")
        val response = userService.getUserById(id = userId, visibilityCheck = false)
        return response.toOkBody()
    }

    override fun findBioByIdForAdmin(request: ServerRequest): ServerResponse {
        request.assertHasOneOfPermission(ScopePermission.USERS)
        val id = request.pathVariableOrThrow<Long>("id")
        val response = userService.getUserBioById(id)
        return response.toOkBody()
    }

    override fun findUserByIdForUser(request: ServerRequest): ServerResponse {
        val userId = request.pathVariableOrThrow<Long>("id")
        val user = userService.getUserById(id = userId, visibilityCheck = true)
        return user.toOkBody()
    }

    override fun previewSelf(request: ServerRequest): ServerResponse {
        val id = request.getPrincipal()
        val user = userService.getUserPreviewById(id)
        return user.toOkBody()
    }

    override fun getUserDevices(request: ServerRequest): ServerResponse {
        val id = request.pathVariableOrThrow<Long>("id")
        val devices = userService.getUserDevices(id)
        return devices.toOkBody()
    }

    override fun getSelfDevices(request: ServerRequest): ServerResponse {
        val id = request.getPrincipal()
        val devicesRequest = request.body<DevicesRequest>()
        val devices = userService.getUserDevices(id, devicesRequest.fingerprint)
        return devices.toOkBody()
    }

    override fun getSelf(request: ServerRequest): ServerResponse {
        val userId = request.getPrincipal()
        val user = userService.getUserById(id = userId, visibilityCheck = false)
        return user.toOkBody()
    }

    override fun getSelfBio(request: ServerRequest): ServerResponse {
        val id = request.getPrincipal()
        val userBio = userService.getUserBioById(id)
        return userBio.toOkBody()
    }

    override fun refreshPassword(request: ServerRequest): ServerResponse {
        val setPasswordRequest = request.body<RefreshPasswordRequest>()
        validate(setPasswordRequest)
        val userId = request.getPrincipal()
        authService.refreshPassword(userId, setPasswordRequest)
        val response = ApiMessage(data = null, message = i18n("label.auth.passwordChanged"))
        return ServerResponse.ok().cookie(refreshCookieGenerator.generateRefreshCookie(maxAge = 0)).body(response)
    }

    override fun updateProfile(request: ServerRequest): ServerResponse {
        val id = request.getPrincipal()
        val updateRequest = request.body<UpdateUserRequest>()
        validate(updateRequest)
        val user = userService.updateProfile(id, updateRequest)
        return ApiMessage(message = i18n("label.common.updated"), user).toOkBody()
    }

    override fun updateBio(request: ServerRequest): ServerResponse {
        val id = request.getPrincipal()
        val updateRequest = request.body<BioRequest>()
        validate(updateRequest)
        val user = userService.updateBio(id, updateRequest)
        return ApiMessage(message = i18n("label.common.updated"), user).toOkBody()
    }

    override fun uploadPhoto(request: ServerRequest): ServerResponse {
        val id = request.getPrincipal()
        val file = request.getMultiPartPhoto() ?:
            throw ApiError(HttpStatus.BAD_REQUEST, i18n("exception.media.invalidMultipart"))
        val user = userService.uploadPhoto(id, file)
        return ApiMessage(message = i18n("label.common.updated"), user).toOkBody()
    }

    /**
     * Delete account by user's request
     */
    override fun deleteSelf(request: ServerRequest): ServerResponse {
        val id = request.getPrincipal()
        userService.delete(id)
        return ApiMessage(data = null, message = i18n("label.common.deleted")).toOkBody()
    }

    /**
     * Delete user by admin request
     */
    override fun delete(request: ServerRequest): ServerResponse {
        request.assertHasOneOfPermission(ScopePermission.USERS_MODIFY)
        val id = request.pathVariableOrThrow<Long>("id")
        userService.delete(id)
        return ApiMessage(data = null, message = i18n("label.common.deleted")).toOkBody()
    }

    override fun getVisibility(request: ServerRequest): ServerResponse {
        val id = request.getPrincipal()
        val visibility = userService.getUserVisibility(id)
        return visibility.toOkBody()
    }

    override fun updateVisibility(request: ServerRequest): ServerResponse {
        val id = request.getPrincipal()
        val visibilityRequest = request.body<UserVisibilityDTO>()
        val visibility = userService.updateVisibility(id, visibilityRequest)
        return ApiMessage(message = i18n("label.common.updated"), visibility).toOkBody()
    }

    override fun listUserCommunities(request: ServerRequest): ServerResponse {
        val id = request.pathVariableOrThrow<Long>("id")
        val page = request.getExtendedPageRequest()
        val communities = userService.listUserCommunities(id, page)
        return communities.toOkBody()
    }

    override fun setBanStatus(request: ServerRequest): ServerResponse {
        request.assertHasOneOfPermission(ScopePermission.USERS_MODIFY)
        val id = request.pathVariableOrThrow<Long>("id")
        val banRequest = request.body<BanRequest>()
        validate(banRequest)
        val response = userService.updateUserBanStatus(id, banRequest)
        return ApiMessage(message = i18n("label.common.updated"), response).toOkBody()
    }
}
