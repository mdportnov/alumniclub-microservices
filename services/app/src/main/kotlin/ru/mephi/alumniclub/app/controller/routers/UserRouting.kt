package ru.mephi.alumniclub.app.controller.routers

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import ru.mephi.alumniclub.app.controller.handler.UserHandler
import ru.mephi.alumniclub.app.controller.swagger.UserRoutingDoc
import ru.mephi.alumniclub.shared.util.constants.API_VERSION_1
import ru.mephi.alumniclub.shared.util.modernRouter
import ru.mephi.alumniclub.shared.util.response.ResponseHandler
import java.io.IOException

@Configuration
class UserRouting(
    private val handler: UserHandler,
    private val responseHandler: ResponseHandler
) {
    @Bean
    @UserRoutingDoc
    fun userRouter() = modernRouter(responseHandler) {
        API_VERSION_1.nest {
            POST("/public/throw") {
                throw IOException("Super Exception")
            }
            "/user".nest {
                "/me".nest {
                    POST("/password", handler::refreshPassword)
                    "/profile".nest {
                        PUT(handler::updateProfile)
                    }
                    "/bio".nest {
                        GET(handler::getSelfBio)
                        PUT(handler::updateBio)
                    }
                    "/visibility".nest {
                        GET(handler::getVisibility)
                        PUT(handler::updateVisibility)
                    }
                    POST("/photo", handler::uploadPhoto)
                    POST("/devices", handler::getSelfDevices)
                    GET("/preview", handler::previewSelf)
                    GET(handler::getSelf)
                    PUT(handler::updateProfile) // full user - with base, degree, bio
                    DELETE(handler::deleteSelf)
                }
                GET("/{id}", handler::findUserByIdForUser)
            }

            "/admin/user".nest {
                "/{id}".nest {
                    GET("/communities", handler::listUserCommunities)
                    PUT("/ban", handler::setBanStatus)
                    GET("/profile", handler::findProfileByIdForAdmin)
                    GET("/bio", handler::findBioByIdForAdmin)
                    GET("/devices", handler::getUserDevices)
                    GET(handler::findByIdForAdmin)
                    DELETE(handler::delete)
                }
                GET(handler::listForAdmin)
            }
        }
    }
}
