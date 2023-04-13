package ru.mephi.alumniclub.app.model.dto.user.response

import ru.mephi.alumniclub.app.model.dto.project.response.ProjectPermissionResponse
import ru.mephi.alumniclub.shared.dto.security.ScopePermission

class PermissionsResponse(
    val scopes: List<ScopePermission> = emptyList(),
    val projects: List<ProjectPermissionResponse> = emptyList()
)