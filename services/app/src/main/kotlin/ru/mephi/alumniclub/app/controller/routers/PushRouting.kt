package ru.mephi.alumniclub.app.controller.routers

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import ru.mephi.alumniclub.app.controller.handler.PushHandler
import ru.mephi.alumniclub.app.controller.swagger.PushRoutingDoc
import ru.mephi.alumniclub.shared.util.constants.API_VERSION_1
import ru.mephi.alumniclub.shared.util.modernRouter
import ru.mephi.alumniclub.shared.util.response.ResponseHandler

@Configuration
class PushRouting(
    private val handler: PushHandler,
    private val responseHandler: ResponseHandler
) {
    @Bean
    @PushRoutingDoc
    fun pushRouter() = modernRouter(responseHandler) {
        "$API_VERSION_1/admin/push".nest {
            POST("/send", handler::sendPushNotification)
        }
    }
}
