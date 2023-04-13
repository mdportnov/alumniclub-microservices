package ru.mephi.alumniclub.app.model.dto.feed.response.user

import ru.mephi.alumniclub.app.model.dto.feed.response.FeedResponse
import ru.mephi.alumniclub.shared.dto.AbstractCreatedAtResponse
import java.time.LocalDateTime
import java.util.*

open class EventResponsePreview(
    id: UUID,
    publicationDate: LocalDateTime,
    val title: String,
    var content: String,
    var feed: FeedResponse,
    val humanUrl: String,
    val time: LocalDateTime
) : AbstractCreatedAtResponse<UUID>(id, publicationDate)
