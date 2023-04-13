package ru.mephi.alumniclub.app.controller.routers

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import ru.mephi.alumniclub.app.controller.handler.RegistrationLimiterHandler
import ru.mephi.alumniclub.app.controller.swagger.RegistrationLimiterRoutingDoc
import ru.mephi.alumniclub.shared.util.constants.API_VERSION_1
import ru.mephi.alumniclub.shared.util.modernRouter
import ru.mephi.alumniclub.shared.util.response.ResponseHandler

@Configuration
class RegistrationLimiterRouting(
    private val handler: RegistrationLimiterHandler,
    private val responseHandler: ResponseHandler
) {
    @Bean
    @RegistrationLimiterRoutingDoc
    fun registrationLimiterRouter() = modernRouter(responseHandler) {
        "$API_VERSION_1/admin".nest {
            "/settings/registration-limiter".nest {
                PUT(handler::setSettings)
                GET(handler::getSettings)
            }
        }
    }
}