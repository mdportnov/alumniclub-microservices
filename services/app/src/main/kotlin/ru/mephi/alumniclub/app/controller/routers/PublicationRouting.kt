package ru.mephi.alumniclub.app.controller.routers

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import ru.mephi.alumniclub.app.controller.handler.PublicationHandler
import ru.mephi.alumniclub.app.controller.swagger.PublicationRoutingDoc
import ru.mephi.alumniclub.shared.util.constants.API_VERSION_1
import ru.mephi.alumniclub.shared.util.modernRouter
import ru.mephi.alumniclub.shared.util.response.ResponseHandler

@Configuration
class PublicationRouting(
    private val handler: PublicationHandler,
    private val responseHandler: ResponseHandler
) {

    @Bean
    @PublicationRoutingDoc
    fun publicationRouter() = modernRouter(responseHandler) {
        API_VERSION_1.nest {
            "/public/feed".nest {
                GET("/default/{feedName}/publication", handler::listPublicationsByDefaultFeedNameForPublic)
                GET("/publication", handler::listAllPublications)
                GET("/publication/{url}", handler::findPublicationByHumanUrl)
                GET("/publication/id/{id}", handler::findPublicationByIdForPublic)
                GET("/event/preview", handler::previewEvents)
                GET("/event/{id}", handler::findEventByIdForPublic)
                GET("/event/url/{url}", handler::findEventByHumanUrl)
                GET("/publication-broadcast/redirect/{id}", handler::redirectToPublicationById)
            }
            "/feed".nest {
                GET("/list", handler::listPublicationFeeds)
                GET("/event", handler::listAllEventsForUser)
                "/default/{feedName}/publication".nest {
                    "/{publicationId}".nest {
                        POST("/like", handler::likeInDefaultFeed)
                        GET(handler::findPublicationByIdInDefaultFeedForUser)
                    }
                    GET(handler::listPublicationsByDefaultFeedNameForUser)
                }
                "/{feedId}".nest {
                    "/publication".nest {
                        "/{publicationId}".nest {
                            POST("/like", handler::like)
                            GET(handler::findPublicationByIdForUser)
                        }
                        GET(handler::listPublicationsByFeedIdForUser)
                    }
                    "/event".nest {
                        "/{publicationId}".nest {
                            POST("/like", handler::like)
                            POST("/participate", handler::participate)
                            GET(handler::findEventByIdForUser)
                        }
                        GET(handler::listEventsByFeedIdForUser)
                    }
                }
            }

            "/meeting/{id}".nest {
                POST("/join", handler::joinMeeting)
                POST("/leave", handler::leaveMeeting)
            }

            "/admin/feed".nest {
                GET("/event", handler::listAllEventsForAdmin)
                GET("/list", handler::listPublicationFeeds)
                "/default/{feedName}/publication".nest {
                    "/{publicationId}".nest {
                        GET("/like", handler::listLikesInDefaultFeed)
                        POST("/photo", handler::uploadPublicationPhotoInDefaultFeed)
                        GET(handler::findPublicationByIdInDefaultFeedForAdmin)
                        PUT(handler::updatePublicationInDefaultFeed)
                        DELETE(handler::deletePublicationInDefaultFeed)
                    }
                    GET(handler::listPublicationsByDefaultFeedNameForAdmin)
                    POST(handler::postPublicationInDefaultFeed)
                }
                "/{feedId}".nest {
                    "/publication".nest {
                        "/{publicationId}".nest {
                            GET("/like", handler::listLikes)
                            POST("/photo", handler::uploadPublicationPhoto)
                            GET(handler::findPublicationByIdForAdmin)
                            PUT(handler::updatePublication)
                            DELETE(handler::deletePublication)
                        }
                        GET(handler::listPublicationsByFeedIdForAdmin)
                        POST(handler::postPublication)
                    }
                    "/event".nest {
                        "/{publicationId}".nest {
                            GET("/like", handler::listLikes)
                            GET("/join", handler::listJoins)
                            POST("/photo", handler::uploadEventPhoto)
                            GET(handler::findEventByIdForAdmin)
                            PUT(handler::updateEvent)
                            DELETE(handler::deleteEvent)
                        }
                        GET(handler::listEventsByFeedIdForAdmin)
                        POST(handler::postEvent)
                    }
                }
            }
        }
    }
}
