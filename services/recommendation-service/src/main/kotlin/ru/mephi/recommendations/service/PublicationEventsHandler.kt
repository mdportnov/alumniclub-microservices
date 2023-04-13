package ru.mephi.recommendations.service

import ru.mephi.recommendations.model.dto.feedback.UserFeedbackRequest
import ru.mephi.recommendations.model.dto.publication_events.AbstractPublicationEventRequest

interface PublicationEventsHandler {
    fun handleUserFeedback(request: UserFeedbackRequest)
    fun handlePublicationEvent(request: AbstractPublicationEventRequest)
}