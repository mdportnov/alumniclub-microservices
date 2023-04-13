package ru.mephi.alumniclub.app.controller.routers

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import ru.mephi.alumniclub.app.controller.handler.CommunityHandler
import ru.mephi.alumniclub.app.controller.swagger.CommunityRoutingDoc
import ru.mephi.alumniclub.shared.util.constants.API_VERSION_1
import ru.mephi.alumniclub.shared.util.modernRouter
import ru.mephi.alumniclub.shared.util.response.ResponseHandler

@Configuration
class CommunityRouting(
    private val handler: CommunityHandler,
    private val responseHandler: ResponseHandler
) {
    @Bean
    @CommunityRoutingDoc
    fun communityRouter() = modernRouter(responseHandler) {
        API_VERSION_1.nest {
            "/community".nest {
                GET("/my", handler::listSelf)
                "/{id}".nest {
                    GET("/members", handler::listMembersForUser)
                    POST("/participate", handler::participate)
                    GET(handler::findById)
                }
                GET(handler::listNotSelf)
            }
            "/admin/community".nest {
                "/{id}".nest {
                    POST("/photo", handler::uploadPhoto)
                    GET("/members", handler::listMembersForAdmin)
                    DELETE("/kick", handler::kick)
                    GET(handler::findById)
                    PUT(handler::update)
                    DELETE(handler::delete)
                }
                GET(handler::list)
                POST(handler::create)
            }
        }
    }
}
