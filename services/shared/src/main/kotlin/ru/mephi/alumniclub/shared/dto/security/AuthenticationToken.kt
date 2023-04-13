package ru.mephi.alumniclub.shared.dto.security

import org.springframework.http.HttpStatus
import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.core.GrantedAuthority
import ru.mephi.alumniclub.shared.dto.common.ApiError
import ru.mephi.alumniclub.shared.model.enums.Authority

class AuthenticationToken(
    private val principal: Long,
    permissions: PermissionsDTO?,
    authorities: List<GrantedAuthority>
) : AbstractAuthenticationToken(authorities) {

    private val permissions = permissions ?: PermissionsDTO()

    override fun getCredentials() = null

    override fun getPrincipal() = principal

    fun getPermissions() = permissions

    fun hasOneOfPermission(vararg permissions: ScopePermission): Boolean {
        if (Authority.ADMIN.grantedAuthority in authorities) return true
        return permissions.toSet().intersect(this.permissions.scopes.toSet()).isNotEmpty()
    }

    fun assertHasOneOfPermission(vararg permissions: ScopePermission) {
        if (!hasOneOfPermission(*permissions))
            throw ApiError(HttpStatus.FORBIDDEN, "exception.forbidden.resource")
    }

    fun assertHasAuthority(authority: Authority) {
        if (authority.grantedAuthority !in authorities)
            throw ApiError(HttpStatus.FORBIDDEN, "exception.forbidden.resource")
    }
}
