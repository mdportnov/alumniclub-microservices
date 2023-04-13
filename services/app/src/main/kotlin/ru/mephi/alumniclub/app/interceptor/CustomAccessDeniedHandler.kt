package ru.mephi.alumniclub.app.interceptor

import org.springframework.http.HttpStatus
import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.stereotype.Component
import ru.mephi.alumniclub.shared.dto.common.ApiError
import ru.mephi.alumniclub.shared.util.response.ResponseManager
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


@Component
class CustomAccessDeniedHandler(
    private var exceptionResolver: ExceptionResolver
) : ResponseManager(), AccessDeniedHandler {
    override fun handle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        accessDeniedException: org.springframework.security.access.AccessDeniedException?
    ) {
        val error = ApiError(HttpStatus.FORBIDDEN, i18n("exception.forbidden.authority"))
        exceptionResolver.resolveException(request, response, error)
    }
}
