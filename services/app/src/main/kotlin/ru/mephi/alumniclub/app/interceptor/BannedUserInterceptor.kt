package ru.mephi.alumniclub.app.interceptor

import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor
import ru.mephi.alumniclub.app.service.UserService
import ru.mephi.alumniclub.shared.dto.common.ApiError
import ru.mephi.alumniclub.shared.util.extension.containsAnyPath
import ru.mephi.alumniclub.shared.util.extension.getPrincipal
import ru.mephi.alumniclub.shared.util.response.ResponseManager
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class BannedUserInterceptor(
    private var userService: UserService,
    private var exceptionResolver: ExceptionResolver,
) : ResponseManager(), HandlerInterceptor {
    private fun shouldSkip(request: HttpServletRequest): Boolean {
        return request.method == "GET" || request.requestURI.containsAnyPath("/user", "/public", "/error")
    }

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        if (shouldSkip(request)) return true

        val principal = request.getPrincipal()

        if (userService.isUserBanned(principal)) {
            exceptionResolver.resolveException(
                request, response, ApiError(HttpStatus.FORBIDDEN, i18n("exception.forbidden.userBanned"))
            )
            return false
        }
        return true
    }
}
