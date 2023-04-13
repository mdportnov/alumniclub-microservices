package ru.mephi.alumniclub.app.controller.routers

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import ru.mephi.alumniclub.app.controller.handler.ExcelHandler
import ru.mephi.alumniclub.app.controller.swagger.ExcelRoutingDoc
import ru.mephi.alumniclub.shared.util.constants.API_VERSION_1
import ru.mephi.alumniclub.shared.util.modernRouter
import ru.mephi.alumniclub.shared.util.response.ResponseHandler

@Configuration
class ExcelRouting(
    private val handler: ExcelHandler,
    private val responseHandler: ResponseHandler
) {
    @Bean
    @ExcelRoutingDoc
    fun excelRouter() = modernRouter(responseHandler) {
        API_VERSION_1.nest {
            "/admin/export".nest {
                GET("/users", handler::exportUsers)
                GET("/event/{eventId}/participants", handler::exportEventParticipants)
            }
        }
    }
}
