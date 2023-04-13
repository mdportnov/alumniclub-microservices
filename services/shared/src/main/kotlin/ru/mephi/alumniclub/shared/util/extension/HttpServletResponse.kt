package ru.mephi.alumniclub.shared.util.extension

import org.springframework.http.MediaType
import org.springframework.security.core.GrantedAuthority
import ru.mephi.alumniclub.shared.dto.security.AuthenticationToken
import ru.mephi.alumniclub.shared.model.enums.Authority
import ru.mephi.alumniclub.shared.model.exceptions.common.PrincipalNotFoundException
import javax.servlet.http.HttpServletRequest

fun HttpServletRequest.isMultipart(): Boolean {
    return this.contentType?.contains(MediaType.MULTIPART_FORM_DATA_VALUE) ?: false
}

fun HttpServletRequest.getPrincipal(): Long {
    if (userPrincipal == null) throw PrincipalNotFoundException()
    return (userPrincipal as AuthenticationToken).principal
}

fun HttpServletRequest.getAuthorities(): List<GrantedAuthority> {
    val token = userPrincipal as AuthenticationToken
    return token.authorities.toList()
}

fun HttpServletRequest.isAdmin(): Boolean {
    return Authority.ADMIN.grantedAuthority in this.getAuthorities()
}