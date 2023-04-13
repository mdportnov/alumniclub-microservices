package ru.mephi.alumniclub.app.controller.routers

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import ru.mephi.alumniclub.app.controller.handler.VersionHandler
import ru.mephi.alumniclub.shared.util.constants.API_VERSION_1
import ru.mephi.alumniclub.shared.util.modernRouter
import ru.mephi.alumniclub.shared.util.response.ResponseHandler

@Configuration
class VersionRouting(
    private val handle: VersionHandler,
    private val responseHandler: ResponseHandler
) {
    @Bean
    fun versionRouter() = modernRouter(responseHandler) {
        API_VERSION_1.nest {
            "/public/versions".nest {
                GET("/isUpdateRequired", handle::isUpdateRequired)
                GET("/minClientApiVersion", handle::getMinSupportedAPIVersion)
                GET("/backendVersion", handle::getBackendVersion)
            }
        }
    }
}