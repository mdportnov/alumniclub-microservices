package ru.mephi.alumniclub.app.controller.routers

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import ru.mephi.alumniclub.app.controller.handler.ProjectHandler
import ru.mephi.alumniclub.app.controller.swagger.ProjectRoutingDoc
import ru.mephi.alumniclub.shared.util.constants.API_VERSION_1
import ru.mephi.alumniclub.shared.util.modernRouter
import ru.mephi.alumniclub.shared.util.response.ResponseHandler

@Configuration
class ProjectRouting(
    private val handler: ProjectHandler,
    private val responseHandler: ResponseHandler
) {
    @Bean
    @ProjectRoutingDoc
    fun projectRouter() = modernRouter(responseHandler) {
        API_VERSION_1.nest {
            GET("/public/project", handler::listProjectsForPublic)
            "/project".nest {
                GET("/my", handler::listByUser)
                "/{id}".nest {
                    POST("/participate", handler::participate)
                    GET("/members", handler::listMembersForUser)
                    GET("/preview", handler::previewProjectById)
                    GET(handler::findProjectByIdForUser)
                }
                GET(handler::listForUser)
            }

            "/admin/project".nest {
                "/{id}".nest {
                    GET("/members", handler::listMembersForAdmin)
                    POST("/photo", handler::uploadPhoto)
                    PUT("/archive", handler::archive)
                    GET(handler::findProjectByIdForAdmin)
                    PUT(handler::update)
                    DELETE(handler::delete)
                }
                GET(handler::listForAdmin)
                POST(handler::create)
            }
        }
    }
}
