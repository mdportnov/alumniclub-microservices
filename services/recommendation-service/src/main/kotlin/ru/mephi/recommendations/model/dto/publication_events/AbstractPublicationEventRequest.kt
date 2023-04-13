package ru.mephi.recommendations.model.dto.publication_events

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import java.util.*

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.EXISTING_PROPERTY,
    property = "type",
    visible = true
)
@JsonSubTypes(
    JsonSubTypes.Type(value = PublicationCreatedEventRequest::class, name = "CREATE"),
    JsonSubTypes.Type(value = PublicationUpdatedEventRequest::class, name = "UPDATE"),
    JsonSubTypes.Type(value = PublicationDeletedEventRequest::class, name = "DELETE")
)
sealed class AbstractPublicationEventRequest(
    val id: UUID,
    val type: PublicationEventType,
)