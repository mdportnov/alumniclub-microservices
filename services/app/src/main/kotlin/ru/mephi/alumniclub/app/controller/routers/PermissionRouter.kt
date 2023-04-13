package ru.mephi.alumniclub.app.controller.routers

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import ru.mephi.alumniclub.app.controller.handler.PermissionHandler
import ru.mephi.alumniclub.shared.util.constants.API_VERSION_1
import ru.mephi.alumniclub.shared.util.modernRouter
import ru.mephi.alumniclub.shared.util.response.ResponseHandler

@Configuration
class PermissionRouter(
    private val handler: PermissionHandler,
    private val responseHandler: ResponseHandler
) {
    @Bean
    fun permissionRouting() = modernRouter(responseHandler) {
        API_VERSION_1.nest {
            "/admin/permission".nest {
                PUT("/admin", handler::updateAdminRole)
                "/moderator".nest {
                    GET("/self", handler::findSelf)
                    "/{id}".nest {
                        GET(handler::findById)
                        PUT(handler::updatePermissions)
                    }
                    GET(handler::list)
                }
            }
        }
    }
}
