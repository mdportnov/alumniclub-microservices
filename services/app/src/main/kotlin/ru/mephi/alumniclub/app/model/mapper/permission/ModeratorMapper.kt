package ru.mephi.alumniclub.app.model.mapper.permission

import org.springframework.data.domain.Page
import org.springframework.stereotype.Component
import ru.mephi.alumniclub.app.database.entity.user.User
import ru.mephi.alumniclub.app.model.dto.PageResponse
import ru.mephi.alumniclub.app.model.dto.project.response.ProjectPermissionResponse
import ru.mephi.alumniclub.app.model.dto.user.response.ModeratorResponse
import ru.mephi.alumniclub.app.model.dto.user.response.PermissionsResponse
import ru.mephi.alumniclub.app.model.exception.common.ResourceNotFoundException
import ru.mephi.alumniclub.app.service.ProjectService
import ru.mephi.alumniclub.shared.dto.common.ApiError
import ru.mephi.alumniclub.shared.dto.security.PermissionsDTO

@Component
class ModeratorMapper(
    private val projectService: ProjectService
) {
    fun asResponse(user: User): ModeratorResponse {
        return ModeratorResponse(
            id = user.id,
            createdAt = user.createdAt,
            name = user.name,
            surname = user.surname,
            patronymic = user.patronymic,
            permissions = asPermissionResponse(user.permissions),
            photoPath = user.photoPath
        )
    }

    fun asPageResponse(users: Page<User>): PageResponse<ModeratorResponse> {
        return PageResponse(
            content = users.content.map(::asResponse),
            number = users.number.toLong(),
            numberOfElements = users.numberOfElements.toLong(),
            totalPages = users.totalPages.toLong()
        )
    }

    private fun asPermissionResponse(dto: PermissionsDTO): PermissionsResponse {
        return PermissionsResponse(
            scopes = dto.scopes,
            projects = dto.projects.map(::asProjectPermissionResponse)
        )
    }

    private fun asProjectPermissionResponse(projectId: Long): ProjectPermissionResponse {
        val name = projectService.findAbstractProjectNameById(projectId)
        return ProjectPermissionResponse(
            id = projectId,
            name = name
        )
    }
}
