package ru.mephi.alumniclub.app.interceptor

import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor
import org.springframework.web.servlet.ModelAndView
import ru.mephi.alumniclub.app.service.UserActivityService
import ru.mephi.alumniclub.shared.util.extension.containsAnyPath
import ru.mephi.alumniclub.shared.util.extension.getPrincipal
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class ActivityInterceptor(
    private val service: UserActivityService,
    private var exceptionResolver: ExceptionResolver,
) : HandlerInterceptor {

    private fun shouldSkip(request: HttpServletRequest): Boolean {
        return request.requestURI.containsAnyPath("/public", "/error")
    }

    override fun postHandle(
        request: HttpServletRequest, response: HttpServletResponse, handler: Any, modelAndView: ModelAndView?
    ) {
        try {
            if (!shouldSkip(request))
                service.handle(request.getPrincipal())
        } catch (ex: Exception) {
            exceptionResolver.resolveException(request, response, ex)
        } finally {
            super.postHandle(request, response, handler, modelAndView)
        }
    }
}