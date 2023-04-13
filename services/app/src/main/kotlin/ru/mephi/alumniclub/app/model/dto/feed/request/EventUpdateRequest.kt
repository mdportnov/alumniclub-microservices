package ru.mephi.alumniclub.app.model.dto.feed.request

import ru.mephi.alumniclub.app.controller.validator.event.EventTimeConstraint
import java.time.LocalDateTime
import javax.validation.constraints.Pattern
import javax.validation.constraints.Size

@EventTimeConstraint
open class EventUpdateRequest(
    @field:Size(max = 600)
    var title: String,
    @field:Size(max = 60000)
    val content: String,
    @field:Pattern(regexp = "[a-z\\d-]+")
    @field:Size(max = 600)
    val humanUrl: String,
    @field:Size(max = 300)
    var place: String? = null,
    val time: LocalDateTime,
    @field:Size(max = 60)
    val tag: String = "",
    @field:Size(max = 120)
    val externalRegistrationLink: String? = null,
    val hidden: Boolean = false,
    var registrationIsOpen: Boolean = true,
    publicationDate: LocalDateTime? = null,
) {
    var publicationDate: LocalDateTime = publicationDate ?: LocalDateTime.now()
}
