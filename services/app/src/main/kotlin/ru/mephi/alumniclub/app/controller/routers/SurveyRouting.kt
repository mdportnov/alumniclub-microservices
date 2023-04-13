package ru.mephi.alumniclub.app.controller.routers

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import ru.mephi.alumniclub.app.controller.handler.SurveyHandler
import ru.mephi.alumniclub.app.controller.swagger.SurveyRoutingDoc
import ru.mephi.alumniclub.shared.util.constants.API_VERSION_1
import ru.mephi.alumniclub.shared.util.modernRouter
import ru.mephi.alumniclub.shared.util.response.ResponseHandler

@Configuration
class SurveyRouting(
    private val handler: SurveyHandler,
    private val responseHandler: ResponseHandler
) {
    @Bean
    @SurveyRoutingDoc
    fun surveyRouter() = modernRouter(responseHandler) {
        API_VERSION_1.nest {
            "/survey".nest {
                GET("/list", handler::cursorList)
                "/{id}".nest {
                    GET("/metadata", handler::getSurveyMetadata)
                    GET("/info", handler::getSurveyAnswersInfo)
                    GET("/answers/me", handler::getSelfAnswer)
                    POST("/vote", handler::vote)
                    GET(handler::getById)
                }
            }
            "/admin/survey".nest {
                GET("/list", handler::pageList)
                "/{id}".nest {
                    "/answers".nest {
                        GET("/user/{userId}", handler::getAnswerOfUser)
                        GET("/variant/{variantId}", handler::getUsersByVariant)
                        GET(handler::getAnswersList)

                    }
                    POST("/photo", handler::uploadPhoto)
                    DELETE(handler::delete)
                    PUT(handler::update)
                    GET(handler::getById)
                }
                POST(handler::create)
            }
        }
    }
}