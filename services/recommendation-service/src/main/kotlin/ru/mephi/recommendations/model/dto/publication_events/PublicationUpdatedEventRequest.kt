package ru.mephi.recommendations.model.dto.publication_events

import java.util.*

class PublicationUpdatedEventRequest(
    id: UUID,
    val title: String,
    val content: String,
) : AbstractPublicationEventRequest(id, PublicationEventType.UPDATE)