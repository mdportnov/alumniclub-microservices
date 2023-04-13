package ru.mephi.alumniclub.app.service.impl.auth.token

import ru.mephi.alumniclub.shared.dto.security.PermissionsDTO
import ru.mephi.alumniclub.shared.dto.security.ScopePermission
import ru.mephi.alumniclub.shared.model.enums.Authority

class UserPrincipal(
    val userId: Long,
    val permissions: PermissionsDTO,
    val authorities: List<Authority>
)
