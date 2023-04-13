package ru.mephi.alumniclub.app.controller.routers

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import ru.mephi.alumniclub.app.controller.handler.FeatureToggleHandler
import ru.mephi.alumniclub.app.controller.swagger.FeatureToggleRoutingDoc
import ru.mephi.alumniclub.shared.util.constants.API_VERSION_1
import ru.mephi.alumniclub.shared.util.modernRouter
import ru.mephi.alumniclub.shared.util.response.ResponseHandler

@Configuration
class FeatureToggleRouting(
    private val handler: FeatureToggleHandler,
    private val responseHandler: ResponseHandler,
) {
    @Bean
    @FeatureToggleRoutingDoc
    fun featureRouter() = modernRouter(responseHandler) {
        "$API_VERSION_1/admin/feature".nest {
            GET("/{featureName}", handler::getFeatureByName)
            PUT(handler::changeFeatureState)
            GET(handler::listAllFeatures)
        }
    }
}
