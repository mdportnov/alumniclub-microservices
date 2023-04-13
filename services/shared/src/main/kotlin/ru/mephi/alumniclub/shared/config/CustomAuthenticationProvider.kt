package ru.mephi.alumniclub.shared.config

import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import ru.mephi.alumniclub.shared.dto.security.AuthenticationToken

@Component
class CustomAuthenticationProvider : AuthenticationProvider {
    override fun authenticate(authentication: Authentication?) = authentication

    override fun supports(authentication: Class<*>?) = authentication == AuthenticationToken::class.java
}