package ru.mephi.alumniclub.app.interceptor

import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor
import ru.mephi.alumniclub.app.security.IRateLimiter
import ru.mephi.alumniclub.shared.util.extension.containsAnyPath
import ru.mephi.alumniclub.shared.util.extension.getPrincipal
import ru.mephi.alumniclub.shared.util.extension.isAdmin
import java.util.concurrent.TimeUnit
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


@Component
class RateLimiterInterceptor(
    private val rateLimiter: IRateLimiter,
    private val exceptionResolver: ExceptionResolver
) : HandlerInterceptor {

    private fun isPublic(request: HttpServletRequest): Boolean {
        // /error to handle PrincipalNotFoundException
        return request.requestURI.containsAnyPath("/public", "/error")
    }

    private fun isSwagger(request: HttpServletRequest): Boolean { // /error to handle PrincipalNotFoundException
        return request.requestURI.containsAnyPath(
            "/swagger", "/swagger-ui", "/v3/api-docs"
        )
    }

    private fun isActuator(request: HttpServletRequest): Boolean { // /error to handle PrincipalNotFoundException
        return request.requestURI.containsAnyPath(
            "/actuator", "/prometheus"
        )
    }

    private fun isFromSwagger(request: HttpServletRequest): Boolean {
        val referer = request.getHeader("Referer")
        return referer?.contains("swagger-ui") ?: false
    }

    override fun preHandle(
        request: HttpServletRequest, response: HttpServletResponse, handler: Any
    ): Boolean {
        val probe = try {
            if (isPublic(request) || isActuator(request)) {
                rateLimiter.tryUnAuthorizedAccess(request.remoteAddr)
            } else if (isFromSwagger(request) || isSwagger(request)) {
                rateLimiter.trySwaggerAccess()
            } else {
                rateLimiter.tryAuthorizedAccess(request.getPrincipal().toString(), request.isAdmin())
            }
        } catch (exception: Exception) {
            exceptionResolver.resolveException(request, response, exception)
            return false
        }
        if (probe.isConsumed) {
            response.addHeader("X-Rate-Limit-Remaining", probe.remainingTokens.toString())
            return true
        }
        response.status = HttpStatus.TOO_MANY_REQUESTS.value()
        response.addHeader(
            "X-Rate-Limit-Retry-After-Milliseconds",
            TimeUnit.NANOSECONDS.toMillis(probe.nanosToWaitForRefill).toString()
        )
        val minutesToTryAgain = TimeUnit.NANOSECONDS.toMinutes(probe.nanosToWaitForRefill)
        response.sendError(
            429,
            "Too Many Requests: Try Again in $minutesToTryAgain ${if (minutesToTryAgain > 1) "minutes" else "minute"}"
        )
        return false
    }
}
