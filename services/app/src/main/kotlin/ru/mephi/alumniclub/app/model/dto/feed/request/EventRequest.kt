package ru.mephi.alumniclub.app.model.dto.feed.request

import ru.mephi.alumniclub.app.model.dto.broadcast.request.AbstractBroadcastRequest
import java.time.LocalDateTime
import javax.validation.Valid

class EventRequest(
    title: String,
    content: String,
    humanUrl: String,
    place: String? = null,
    time: LocalDateTime,
    tag: String = "",
    publicationDate: LocalDateTime? = null,
    externalRegistrationLink: String? = null,
    hidden: Boolean = false,
    registrationIsOpen: Boolean = true,
    @field:Valid
    val broadcast: AbstractBroadcastRequest,
) : EventUpdateRequest(
    title = title,
    content = content,
    humanUrl = humanUrl,
    place = place,
    time = time,
    tag = tag,
    publicationDate = publicationDate,
    externalRegistrationLink = externalRegistrationLink,
    hidden = hidden,
    registrationIsOpen = registrationIsOpen
)
