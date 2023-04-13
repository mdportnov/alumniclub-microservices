package ru.mephi.recommendations.model.dto.publication_events

import java.util.*

class PublicationDeletedEventRequest(
    id: UUID,
) : AbstractPublicationEventRequest(id, PublicationEventType.DELETE)