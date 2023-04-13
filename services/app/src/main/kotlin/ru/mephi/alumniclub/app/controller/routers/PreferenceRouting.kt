package ru.mephi.alumniclub.app.controller.routers

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import ru.mephi.alumniclub.app.controller.handler.PreferenceHandler
import ru.mephi.alumniclub.app.controller.swagger.PreferencesRoutingDoc
import ru.mephi.alumniclub.shared.util.constants.API_VERSION_1
import ru.mephi.alumniclub.shared.util.modernRouter
import ru.mephi.alumniclub.shared.util.response.ResponseHandler

@Configuration
class PreferenceRouting(
    private val handler: PreferenceHandler,
    private val responseHandler: ResponseHandler
) {
    @Bean
    @PreferencesRoutingDoc
    fun preferenceRouter() = modernRouter(responseHandler) {
        API_VERSION_1.nest {
            "/preferences".nest {
                GET(handler::getPreferences)
                PUT(handler::updatePreferences)
            }
            "/admin/preferences".nest {
                GET("/{id}", handler::getPreferencesOfUser)
            }
            "/public/preferences/turnOff".nest {
                GET("/{token}", handler::turnOffEmailPreference)
            }
        }
    }
}