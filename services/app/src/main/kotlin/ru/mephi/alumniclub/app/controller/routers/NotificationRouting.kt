package ru.mephi.alumniclub.app.controller.routers

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import ru.mephi.alumniclub.app.controller.handler.NotificationHandler
import ru.mephi.alumniclub.app.controller.swagger.NotificationRoutingDoc
import ru.mephi.alumniclub.shared.util.constants.API_VERSION_1
import ru.mephi.alumniclub.shared.util.modernRouter
import ru.mephi.alumniclub.shared.util.response.ResponseHandler

@Configuration
class NotificationRouting(
    private val handler: NotificationHandler,
    private val responseHandler: ResponseHandler
) {
    @Bean
    @NotificationRoutingDoc
    fun notificationRouter() = modernRouter(responseHandler) {
        API_VERSION_1.nest {
            "/notification".nest {
                GET("/info", handler::getUserNotificationsInfo)
                GET("/{id}", handler::findByIdForUser)
                DELETE("/{id}", handler::deleteForUser)
                GET(handler::listForUser)
            }
            "/admin/notification".nest {
                GET("/{id}", handler::findByIdForAdmin)
                DELETE("/{id}", handler::delete)
                GET(handler::listForAdmin)
            }
        }
    }
}
