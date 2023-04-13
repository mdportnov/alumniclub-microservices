package ru.mephi.alumniclub.app.controller.routers

import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component
import ru.mephi.alumniclub.app.controller.handler.AuthHandler
import ru.mephi.alumniclub.app.controller.swagger.AuthRoutingDoc
import ru.mephi.alumniclub.shared.util.constants.API_VERSION_1
import ru.mephi.alumniclub.shared.util.modernRouter
import ru.mephi.alumniclub.shared.util.response.ResponseHandler

@Component
class AuthRouting(
    private val handler: AuthHandler,
    private val responseHandler: ResponseHandler
) {
    @Bean
    @AuthRoutingDoc
    fun authRouter() = modernRouter(responseHandler) {
        API_VERSION_1.nest {
            "/public/auth".nest {
                POST("/register", handler::register)
                POST("/login", handler::login)
                POST("/refresh", handler::refresh)
                POST("/logout", handler::logout)
                GET("/email/verify", handler::verifyEmail)
                "/password".nest {
                    POST("/reset", handler::resetPassword)
                    POST("/reset/refresh", handler::resetRefreshPassword)
                    POST("/refresh", handler::refreshPassword)
                    GET("/token/exists", handler::checkResetPasswordTokenExists)
                }
            }
        }
    }
}
