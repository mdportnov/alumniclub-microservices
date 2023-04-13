package ru.mephi.alumniclub.app.controller.routers

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import ru.mephi.alumniclub.app.controller.handler.impl.FormHandler
import ru.mephi.alumniclub.app.controller.swagger.FormRoutingDoc
import ru.mephi.alumniclub.shared.util.constants.API_VERSION_1
import ru.mephi.alumniclub.shared.util.modernRouter
import ru.mephi.alumniclub.shared.util.response.ResponseHandler

@Configuration
class FormRouting(
    private val formHandler: FormHandler,
    private val responseHandler: ResponseHandler
) {
    @Bean
    @FormRoutingDoc
    fun formRouter() = modernRouter(responseHandler) {
        API_VERSION_1.nest {
            "/form".nest {
                GET("/types", formHandler::listTypes)
                GET("/self", formHandler::getSelfForms)
                POST(formHandler::saveForm)
                GET(formHandler::getFullForm)
            }
            "/admin/form".nest {
                GET("/types", formHandler::listTypes)
                GET("/user", formHandler::getAllUserForms)
                "/{id}".nest {
                    POST("/status", formHandler::changeStatus)
                    GET(formHandler::getById)
                    DELETE(formHandler::delete)
                }
                accept(MediaType.APPLICATION_JSON).nest {
                    GET(formHandler::getFormsByType)
                }
            }
        }
    }
}
