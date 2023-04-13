package ru.mephi.alumniclub.app.model.dto.feed.response.user

import ru.mephi.alumniclub.app.model.dto.feed.response.FeedResponse
import ru.mephi.alumniclub.shared.dto.AbstractCreatedAtResponse
import ru.mephi.alumniclub.shared.dto.photo.PhotoPathed
import java.time.LocalDateTime
import java.util.*

class PublicationShortResponseForUser(
    id: UUID,
    publicationDate: LocalDateTime,

    val title: String,
    var content: String = "",
    var feed: FeedResponse,
    var liked: Boolean,
    val humanUrl: String,
    var likesCount: Long,
    val viewsCount: Long,

    override var photoPath: String?,
) : AbstractCreatedAtResponse<UUID>(id, publicationDate), PhotoPathed
