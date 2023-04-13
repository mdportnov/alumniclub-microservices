package ru.mephi.alumniclub.app.util

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import ru.mephi.alumniclub.shared.util.constants.REFRESH_TOKEN_COOKIE
import java.time.temporal.ChronoUnit
import javax.servlet.http.Cookie

@Component
class RefreshCookieGenerator(
    @Value("\${spring.security.jwt.refresh.lifetime}")
    private val refreshTokenLifetime: Int
) {
    private val cookieMaxAge = refreshTokenLifetime * ChronoUnit.DAYS.duration.seconds.toInt()
    fun generateRefreshCookie(token: String = "", maxAge: Int = cookieMaxAge): Cookie {
        val cookie = Cookie(REFRESH_TOKEN_COOKIE, token)
        cookie.isHttpOnly = true
        cookie.maxAge = maxAge
        cookie.path = "/"
        return cookie
    }
}
