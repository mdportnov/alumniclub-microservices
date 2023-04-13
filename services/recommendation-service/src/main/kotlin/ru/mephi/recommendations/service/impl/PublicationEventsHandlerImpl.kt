package ru.mephi.recommendations.service.impl

import org.springframework.stereotype.Service
import ru.mephi.recommendations.model.dto.feedback.UserFeedbackRequest
import ru.mephi.recommendations.model.dto.publication_events.AbstractPublicationEventRequest
import ru.mephi.recommendations.model.dto.publication_events.PublicationCreatedEventRequest
import ru.mephi.recommendations.model.dto.publication_events.PublicationDeletedEventRequest
import ru.mephi.recommendations.model.dto.publication_events.PublicationUpdatedEventRequest
import ru.mephi.recommendations.service.PublicationEventsHandler
import ru.mephi.recommendations.service.PublicationService
import java.util.logging.Level
import java.util.logging.Logger

@Service
class PublicationEventsHandlerImpl(
    private val publicationService: PublicationService,
) : PublicationEventsHandler {

    override fun handleUserFeedback(request: UserFeedbackRequest) {
        TODO()
    }

    override fun handlePublicationEvent(request: AbstractPublicationEventRequest) {
        when (request) {
            is PublicationCreatedEventRequest -> publicationService.create(request)
            is PublicationUpdatedEventRequest -> publicationService.update(request)
            is PublicationDeletedEventRequest -> publicationService.delete(request)
        }
    }

}