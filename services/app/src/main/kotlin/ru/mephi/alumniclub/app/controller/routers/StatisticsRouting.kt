package ru.mephi.alumniclub.app.controller.routers

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import ru.mephi.alumniclub.app.controller.handler.StatisticsHandler
import ru.mephi.alumniclub.shared.util.constants.API_VERSION_1
import ru.mephi.alumniclub.shared.util.modernRouter
import ru.mephi.alumniclub.shared.util.response.ResponseHandler

@Configuration
class StatisticsRouting(
    private val handler: StatisticsHandler,
    private val responseHandler: ResponseHandler
) {
    @Bean
    fun statisticsRouter() = modernRouter(responseHandler) {
        GET("$API_VERSION_1/admin/statistics", handler::getStatistics)
    }
}
