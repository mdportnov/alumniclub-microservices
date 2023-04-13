package ru.mephi.alumniclub.shared.dto.security

import ru.mephi.alumniclub.shared.validators.permissions.PermissionsConstraint

@PermissionsConstraint
class PermissionsDTO(
    val scopes: List<ScopePermission> = emptyList(),
    val projects: List<Long> = emptyList()
) {
    fun hasPermissions() = scopes.isNotEmpty() || projects.isNotEmpty()
    fun noPermissions() = scopes.isEmpty() && projects.isEmpty()
}
