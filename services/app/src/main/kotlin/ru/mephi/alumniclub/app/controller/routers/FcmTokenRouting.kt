package ru.mephi.alumniclub.app.controller.routers

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import ru.mephi.alumniclub.app.controller.handler.FcmTokenHandler
import ru.mephi.alumniclub.app.controller.swagger.FcmTokenRoutingDoc
import ru.mephi.alumniclub.shared.util.constants.API_VERSION_1
import ru.mephi.alumniclub.shared.util.modernRouter
import ru.mephi.alumniclub.shared.util.response.ResponseHandler

@Configuration
class FcmTokenRouting(
    private val responseHandler: ResponseHandler,
    private val handler: FcmTokenHandler
) {
    @Bean
    @FcmTokenRoutingDoc
    fun fcmTokenRouter() = modernRouter(responseHandler) {
        "$API_VERSION_1/fcm/tokens".nest {
            accept(MediaType.APPLICATION_JSON).nest {
                POST("/upload", handler::uploadToken)
                DELETE("/remove", handler::removeToken)
            }
        }
    }
}
