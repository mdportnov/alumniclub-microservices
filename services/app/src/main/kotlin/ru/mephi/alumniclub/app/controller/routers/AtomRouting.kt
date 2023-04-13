package ru.mephi.alumniclub.app.controller.routers

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import ru.mephi.alumniclub.app.controller.handler.AtomHandler
import ru.mephi.alumniclub.app.controller.swagger.AtomRoutingDoc
import ru.mephi.alumniclub.shared.util.constants.API_VERSION_1
import ru.mephi.alumniclub.shared.util.modernRouter
import ru.mephi.alumniclub.shared.util.response.ResponseHandler

@Configuration
class AtomRouting(
    private val atomHandler: AtomHandler,
    private val responseHandler: ResponseHandler
) {
    @Bean
    @AtomRoutingDoc
    fun atomRouter() = modernRouter(responseHandler) {
        API_VERSION_1.nest {
            "/atom".nest {
                GET("/my", atomHandler::getSelfAtomHistory)
            }
            "/admin/atom".nest {
                GET(atomHandler::getUserAtomHistory)
                POST("/accrue", atomHandler::accrueAtoms)
                DELETE(atomHandler::deleteAtomHistoryElement)
            }
        }
    }
}
