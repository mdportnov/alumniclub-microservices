package ru.mephi.alumniclub.app.model.dto.feed.request

import ru.mephi.alumniclub.app.model.dto.broadcast.request.AbstractBroadcastRequest
import java.time.LocalDateTime
import javax.validation.Valid

open class PublicationRequest(
    title: String,
    content: String,
    humanUrl: String,
    publicationDate: LocalDateTime? = null,
    hidden: Boolean = false,
    @field:Valid
    val broadcast: AbstractBroadcastRequest,
) : PublicationUpdateRequest(
    title = title,
    content = content,
    humanUrl = humanUrl,
    publicationDate = publicationDate,
    hidden = hidden
)
