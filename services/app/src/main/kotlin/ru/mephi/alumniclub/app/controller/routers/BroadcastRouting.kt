package ru.mephi.alumniclub.app.controller.routers

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import ru.mephi.alumniclub.app.controller.handler.BroadcastHandler
import ru.mephi.alumniclub.app.controller.swagger.BroadcastRoutingDoc
import ru.mephi.alumniclub.shared.util.constants.API_VERSION_1
import ru.mephi.alumniclub.shared.util.modernRouter
import ru.mephi.alumniclub.shared.util.response.ResponseHandler

@Configuration
class BroadcastRouting(
    private val handler: BroadcastHandler,
    private val responseHandler: ResponseHandler
) {
    @Bean
    @BroadcastRoutingDoc
    fun broadcastRouter() = modernRouter(responseHandler) {
        API_VERSION_1.nest {
            "/broadcast".nest {
                "/{id}".nest {
                    GET(handler::getShortById)
                }
            }

            "/admin/broadcast".nest {
                "/{id}".nest {
                    POST("/photo", handler::uploadPhoto)
                    GET(handler::getById)
                    PUT(handler::update)
                    DELETE(handler::delete)
                }
                POST(handler::createBroadcast)
            }
        }
    }
}