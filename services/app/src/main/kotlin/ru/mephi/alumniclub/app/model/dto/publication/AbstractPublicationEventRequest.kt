package ru.mephi.alumniclub.app.model.dto.publication

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import ru.mephi.alumniclub.app.model.dto.publication.AbstractPublicationEventRequest.*
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
) {

    class PublicationCreatedEventRequest(
        id: UUID,
        val title: String,
        val content: String,
    ) : AbstractPublicationEventRequest(id, PublicationEventType.CREATE)

    class PublicationUpdatedEventRequest(
        id: UUID,
        val title: String,
        val content: String,
    ) : AbstractPublicationEventRequest(id, PublicationEventType.UPDATE)

    class PublicationDeletedEventRequest(
        id: UUID,
    ) : AbstractPublicationEventRequest(id, PublicationEventType.DELETE)
}