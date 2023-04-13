package ru.mephi.alumniclub.app.service

import io.swagger.v3.oas.annotations.parameters.RequestBody
import org.springframework.web.bind.annotation.PathVariable
import ru.mephi.alumniclub.app.model.dto.PageResponse
import ru.mephi.alumniclub.app.model.dto.security.AdminDTO
import ru.mephi.alumniclub.shared.dto.security.PermissionsDTO
import ru.mephi.alumniclub.app.model.dto.user.response.ModeratorResponse
import ru.mephi.alumniclub.shared.dto.ExtendedPageRequest

interface PermissionService {
    fun listModerators(query: String, pageRequest: ExtendedPageRequest): PageResponse<ModeratorResponse>
    fun findModeratorById(@PathVariable id: Long): ModeratorResponse
    fun updatePermissions(id: Long, @RequestBody request: PermissionsDTO): ModeratorResponse
    fun updateAdminRole(request: AdminDTO): AdminDTO
}
