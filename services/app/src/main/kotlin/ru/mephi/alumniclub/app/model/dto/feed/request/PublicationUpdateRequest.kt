package ru.mephi.alumniclub.app.model.dto.feed.request

import java.time.LocalDateTime
import javax.validation.constraints.Pattern
import javax.validation.constraints.Size

open class PublicationUpdateRequest(
    @field:Size(max = 600)
    var title: String,
    @field:Size(max = 60000)
    val content: String,
    @field:Pattern(regexp = "[a-z\\d-]+")
    @field:Size(max = 600)
    val humanUrl: String,
    var hidden: Boolean = false,
    publicationDate: LocalDateTime? = null,
) {
    var publicationDate: LocalDateTime = publicationDate ?: LocalDateTime.now()
}
