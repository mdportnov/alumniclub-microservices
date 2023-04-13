package ru.mephi.recommendations.controller.router

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import ru.mephi.alumniclub.shared.util.modernRouter
import ru.mephi.alumniclub.shared.util.response.ResponseHandler
import ru.mephi.recommendations.controller.handler.RecommendationHandler

@Configuration
class RecommendationRouter(
    private val handler: RecommendationHandler,
    private val responseHandler: ResponseHandler
) {
    @Bean
    fun recommendationRouting() = modernRouter(responseHandler) {
        "/v1".nest {
            GET("/publication/{id}", handler::getRecommendationByPublicationId)
        }
    }
}