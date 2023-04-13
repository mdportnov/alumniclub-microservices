package ru.mephi.alumniclub.app.filter

import org.springframework.context.annotation.Lazy
import org.springframework.http.HttpStatus
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import ru.mephi.alumniclub.app.interceptor.ExceptionResolver
import ru.mephi.alumniclub.app.service.impl.auth.token.TokenManager
import ru.mephi.alumniclub.shared.dto.common.ApiError
import ru.mephi.alumniclub.shared.util.constants.API_VERSION_1
import ru.mephi.alumniclub.shared.util.extension.containsAnyPath
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


@Component
class JwtFilter(
    @Lazy
    private val tokenManager: TokenManager,
    private val exceptionResolver: ExceptionResolver
) : OncePerRequestFilter() {

    override fun shouldNotFilter(request: HttpServletRequest): Boolean {
        return request.requestURI.containsAnyPath("/actuator", "/api$API_VERSION_1/public")
    }

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        try {
            val header = request.getHeader("Authorization")
                ?: throw ApiError(HttpStatus.UNAUTHORIZED, "Authorization header not found")
            SecurityContextHolder.getContext().authentication = tokenManager.createAuthToken(header)
            filterChain.doFilter(request, response)
        } catch (exception: ApiError) {
            exceptionResolver.resolveException(request, response, exception)
            // Catching CorruptedToken, PrincipalNotFound
        }
    }
}
