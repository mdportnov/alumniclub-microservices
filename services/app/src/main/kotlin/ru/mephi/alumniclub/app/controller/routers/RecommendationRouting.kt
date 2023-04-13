package ru.mephi.alumniclub.app.controller.routers

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import ru.mephi.alumniclub.app.controller.handler.RecommendationHandler
import ru.mephi.alumniclub.shared.util.constants.API_VERSION_1
import ru.mephi.alumniclub.shared.util.modernRouter
import ru.mephi.alumniclub.shared.util.response.ResponseHandler

@Configuration
class RecommendationRouting(
    private val handler: RecommendationHandler,
    private val responseHandler: ResponseHandler
) {
    @Bean
    fun recommendationRouter() = modernRouter(responseHandler) {
        API_VERSION_1.nest {
            "/recommendation".nest {
                "/content".nest {
                    GET("/{id}", handler::getContentBasedRecommendation)
                }
            }
            "/admin/recommendation".nest {
                POST("/recalculate", handler::recalculateAllPublicationVectors)
            }
        }

    }

}