package ru.mephi.alumniclub.app.controller.handler.impl

import org.springframework.stereotype.Service
import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse
import org.springframework.web.servlet.function.body
import ru.mephi.alumniclub.app.controller.handler.PermissionHandler
import ru.mephi.alumniclub.app.model.dto.security.AdminDTO
import ru.mephi.alumniclub.app.service.PermissionService
import ru.mephi.alumniclub.shared.dto.common.ApiMessage
import ru.mephi.alumniclub.shared.dto.security.PermissionsDTO
import ru.mephi.alumniclub.shared.model.enums.Authority
import ru.mephi.alumniclub.shared.util.extension.*
import ru.mephi.alumniclub.shared.util.response.ResponseManager

@Service
class PermissionHandlerImpl(
    private val permissionService: PermissionService
) : ResponseManager(), PermissionHandler {
    override fun list(request: ServerRequest): ServerResponse {
        request.assertHasAuthority(Authority.ADMIN)
        val query = request.paramOrElse("query") { "" }.trim()
        val pageRequest = request.getExtendedPageRequest()
        val moderators = permissionService.listModerators(query, pageRequest)
        return moderators.toOkBody()
    }

    override fun updateAdminRole(request: ServerRequest): ServerResponse {
        request.assertHasAuthority(Authority.ADMIN)
        val adminRequest = request.body<AdminDTO>()
        return ApiMessage(
            data = permissionService.updateAdminRole(adminRequest),
            message = i18n("label.permission.updateAdminRole")
        ).toOkBody()
    }

    override fun findById(request: ServerRequest): ServerResponse {
        request.assertHasAuthority(Authority.ADMIN)
        val id = request.pathVariableOrThrow<Long>("id")
        val moderator = permissionService.findModeratorById(id)
        return moderator.toOkBody()
    }

    override fun findSelf(request: ServerRequest): ServerResponse {
        val id = request.getPrincipal()
        val moderator = permissionService.findModeratorById(id)
        return moderator.toOkBody()
    }

    override fun updatePermissions(request: ServerRequest): ServerResponse {
        request.assertHasAuthority(Authority.ADMIN)
        val id = request.pathVariableOrThrow<Long>("id")
        val permissionsRequest = request.body<PermissionsDTO>()
        validate(permissionsRequest)
        return ApiMessage(
            data = permissionService.updatePermissions(id, permissionsRequest),
            message = i18n("label.permission.updateAuthorities")
        ).toOkBody()
    }
}
