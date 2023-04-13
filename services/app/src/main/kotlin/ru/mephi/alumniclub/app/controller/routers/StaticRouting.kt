package ru.mephi.alumniclub.app.controller.routers

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import ru.mephi.alumniclub.app.controller.handler.StaticHandler
import ru.mephi.alumniclub.app.controller.swagger.StaticRoutingDoc
import ru.mephi.alumniclub.shared.util.constants.API_VERSION_1
import ru.mephi.alumniclub.shared.util.modernRouter
import ru.mephi.alumniclub.shared.util.response.ResponseHandler

@Configuration
class StaticRouting(
    private val handler: StaticHandler,
    private val responseHandler: ResponseHandler
) {
    @Bean
    @StaticRoutingDoc
    fun staticRouter() = modernRouter(responseHandler) {
        API_VERSION_1.nest {
            "/public".nest {
                GET("/uploads/{dir}/{fileName}", handler::getUploadedPhoto)
                GET("/publication-photo/{id}", handler::getPublicationPhotoById)
            }

            "/admin/uploads".nest {
                "/photo".nest {
                    POST(handler::createContentPhoto)
                }
            }
        }
    }
}
