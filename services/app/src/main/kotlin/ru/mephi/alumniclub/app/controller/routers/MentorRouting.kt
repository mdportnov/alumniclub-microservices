package ru.mephi.alumniclub.app.controller.routers

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import ru.mephi.alumniclub.app.controller.handler.MentorHandler
import ru.mephi.alumniclub.app.controller.swagger.MentorRoutingDoc
import ru.mephi.alumniclub.shared.util.constants.API_VERSION_1
import ru.mephi.alumniclub.shared.util.modernRouter
import ru.mephi.alumniclub.shared.util.response.ResponseHandler

@Configuration
class MentorRouting(
    private val handler: MentorHandler,
    private val responseHandler: ResponseHandler
) {
    @Bean
    @MentorRoutingDoc
    fun mentorRouter() = modernRouter(responseHandler) {
        API_VERSION_1.nest {
            "/public/mentor".nest {
                GET("/{id}", handler::getByIdForPublic)
                GET(handler::listAllAvailable)
            }
            "/mentor".nest {
                GET("/{id}", handler::getByIdForPublic)
                GET(handler::listAllAvailable)
            }
            "/admin/mentor".nest {
                PUT("/{id}/availability", handler::toggleMentorAvailability)
                PUT("/{id}", handler::update)
                DELETE("/{id}", handler::delete)
                GET("/{id}", handler::getByIdForAdmin)
                GET(handler::listAll)
                POST(handler::create)
            }
        }
    }
}
