package ru.mephi.alumniclub.app.controller.routers

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import ru.mephi.alumniclub.app.controller.handler.CarouselHandler
import ru.mephi.alumniclub.app.controller.swagger.CarouselRoutingDoc
import ru.mephi.alumniclub.shared.util.constants.API_VERSION_1
import ru.mephi.alumniclub.shared.util.modernRouter
import ru.mephi.alumniclub.shared.util.response.ResponseHandler

@Configuration
class CarouselRouting(
    private val handler: CarouselHandler,
    private val responseHandler: ResponseHandler
) {
    @Bean
    @CarouselRoutingDoc
    fun carouselRouter() = modernRouter(responseHandler) {
        API_VERSION_1.nest {
            "/news".nest {
                GET("/{id}", handler::getById)
                GET(handler::getAll)
            }
            "/admin/news".nest {
                "/{id}".nest {
                    GET(handler::getById)
                    POST("/photo", handler::uploadPhoto)
                    PUT(handler::update)
                    DELETE(handler::remove)
                }
                POST(handler::create)
                GET(handler::getAll)
            }
        }
    }
}