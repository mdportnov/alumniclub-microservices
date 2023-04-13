package ru.mephi.alumniclub.shared.model.enums

import org.springframework.security.core.GrantedAuthority

enum class Authority(val grantedAuthority: GrantedAuthority) {
    USER(GrantedAuthority { "ROLE_USER" }),
    MENTOR(GrantedAuthority { "ROLE_MENTOR" }),
    MODERATOR(GrantedAuthority { "ROLE_MODERATOR" }),
    ADMIN(GrantedAuthority { "ROLE_ADMIN" })
}
