package ru.mephi.alumniclub.app.interceptor

import org.slf4j.MDC
import org.springframework.stereotype.Component
import ru.mephi.alumniclub.app.service.impl.auth.token.TokenHelper
import java.util.*
import javax.servlet.ServletRequestEvent
import javax.servlet.ServletRequestListener
import javax.servlet.http.HttpServletRequest

@Component
class MdcServletRequestListener(
    private val tokenHelper: TokenHelper
) : ServletRequestListener {
    override fun requestInitialized(requestEvent: ServletRequestEvent) {
        val request: HttpServletRequest = requestEvent.servletRequest as HttpServletRequest
        try {
            MDC.put("requestId", UUID.randomUUID().toString())
            val token = request.getHeader("Authorization")
            if (token.isNullOrBlank()) return
            else MDC.put("userId", tokenHelper.parseTokenPrincipalFromHeader(token).userId.toString())
        } catch (e: Exception) {
            return
        }
    }

    override fun requestDestroyed(requestEvent: ServletRequestEvent) {
        MDC.clear()
    }
}