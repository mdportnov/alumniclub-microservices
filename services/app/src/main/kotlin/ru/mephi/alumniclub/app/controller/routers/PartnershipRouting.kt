package ru.mephi.alumniclub.app.controller.routers

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import ru.mephi.alumniclub.app.controller.handler.PartnershipHandler
import ru.mephi.alumniclub.shared.util.constants.API_VERSION_1
import ru.mephi.alumniclub.shared.util.modernRouter
import ru.mephi.alumniclub.shared.util.response.ResponseHandler

@Configuration
class PartnershipRouting(
    private val handler: PartnershipHandler,
    private val responseHandler: ResponseHandler
) {
    @Bean
    fun partnershipRouter() = modernRouter(responseHandler) {
        API_VERSION_1.nest {
            "/partnership".nest {
                GET("/{id}", handler::findById)
                GET(handler::listForPublic)
            }
            "/public/partnership".nest {
                GET("/{id}", handler::findById)
                GET(handler::listForPublic)
            }
            "/admin/partnership".nest {
                "/{id}".nest {
                    GET("/members", handler::listMembers)
                    POST("/photo", handler::uploadPhoto)
                    GET(handler::findById)
                    PUT(handler::update)
                    DELETE(handler::delete)
                }
                GET(handler::listForAdmin)
                POST(handler::create)
            }
        }
    }
}