package ru.mephi.alumniclub.app.controller.routers

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import ru.mephi.alumniclub.app.controller.handler.MerchHandler
import ru.mephi.alumniclub.app.controller.swagger.MerchRoutingDoc
import ru.mephi.alumniclub.shared.util.constants.API_VERSION_1
import ru.mephi.alumniclub.shared.util.modernRouter
import ru.mephi.alumniclub.shared.util.response.ResponseHandler

@Configuration
class MerchRouting(
    private val handler: MerchHandler,
    private val responseHandler: ResponseHandler
) {
    @Bean
    @MerchRoutingDoc
    fun merchRouter() = modernRouter(responseHandler) {
        API_VERSION_1.nest {
            "/merch".nest {
                GET(handler::getAllAvailableMerch)
            }
            "/admin/merch".nest {
                "/{id}".nest {
                    POST("/photo", handler::uploadPhoto)
                    GET(handler::getMerchById)
                    PUT(handler::updateMerch)
                    DELETE(handler::deleteMerch)
                }
                GET(handler::getAllMerch)
                POST(handler::create)
            }
        }
    }
}
