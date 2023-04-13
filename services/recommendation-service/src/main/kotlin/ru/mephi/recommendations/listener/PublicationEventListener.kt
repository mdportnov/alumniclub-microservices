package ru.mephi.recommendations.listener

import ru.mephi.recommendations.model.dto.feedback.UserFeedbackRequest
import ru.mephi.recommendations.model.dto.publication_events.AbstractPublicationEventRequest

interface PublicationEventListener {
    fun userFeedbackListener(request: UserFeedbackRequest)
    fun publicationEventListener(request: AbstractPublicationEventRequest)
}