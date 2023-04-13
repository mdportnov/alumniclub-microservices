package ru.mephi.recommendations.listener.impl

import org.springframework.amqp.rabbit.annotation.EnableRabbit
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Component
import ru.mephi.alumniclub.shared.logging.AlumniLogger
import ru.mephi.recommendations.listener.PublicationEventListener
import ru.mephi.recommendations.model.dto.feedback.UserFeedbackRequest
import ru.mephi.recommendations.model.dto.publication_events.AbstractPublicationEventRequest
import ru.mephi.recommendations.service.PublicationEventsHandler
import ru.mephi.recommendations.utils.PUBLICATIONS_EVENT_QUEUE

@Component
@EnableRabbit
class PublicationEventListenerImpl(
    private val handler: PublicationEventsHandler,
    private val logger: AlumniLogger
) : PublicationEventListener {

    //    @RabbitListener(queues = [PUBLICATIONS_USERS_FEEDBACK_QUEUE])
    override fun userFeedbackListener(request: UserFeedbackRequest) {
        try {
            handler.handleUserFeedback(request)
        } catch (e: Exception) {
            logger.error("USER FEEDBACK ERROR", e)
        }
    }

    @RabbitListener(queues = [PUBLICATIONS_EVENT_QUEUE])
    override fun publicationEventListener(request: AbstractPublicationEventRequest) {
        logger.info("Gets request of type [${request.type}] for publication with id [${request.id}]")
        try {
            handler.handlePublicationEvent(request)
            logger.info("request of type [${request.type}] for publication with id [${request.id}] completed successfully")
        } catch (e: Exception) {
            logger.error("PUBLICATION EVENT ERROR", e)
        }
    }
}