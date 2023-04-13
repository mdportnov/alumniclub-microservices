package ru.mephi.recommendations.service

import ru.mephi.recommendations.database.milvus.publication.Publication
import ru.mephi.recommendations.model.dto.publication_events.PublicationCreatedEventRequest
import ru.mephi.recommendations.model.dto.publication_events.PublicationDeletedEventRequest
import ru.mephi.recommendations.model.dto.publication_events.PublicationUpdatedEventRequest
import java.util.*

interface PublicationService {
    fun create(request: PublicationCreatedEventRequest): Publication
    fun update(request: PublicationUpdatedEventRequest)
    fun delete(request: PublicationDeletedEventRequest)
    fun findPublicationById(id: UUID): Publication
    fun getNearestPublicationsIds(id: UUID, n: Int = 3): List<UUID>
}