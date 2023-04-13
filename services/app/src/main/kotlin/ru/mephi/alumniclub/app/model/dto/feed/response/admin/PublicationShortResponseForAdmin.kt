package ru.mephi.alumniclub.app.model.dto.feed.response.admin

import ru.mephi.alumniclub.app.model.dto.feed.response.FeedResponse
import ru.mephi.alumniclub.shared.dto.AbstractCreatedAtResponse
import ru.mephi.alumniclub.shared.dto.photo.PhotoPathed
import java.time.LocalDateTime
import java.util.*

class PublicationShortResponseForAdmin(
    id: UUID,
    createdAt: LocalDateTime,

    val publicationDate: LocalDateTime,
    val title: String,
    var feed: FeedResponse,
    val humanUrl: String,
    var likesCount: Long,
    val viewsCount: Long,
    var hidden: Boolean,

    override var photoPath: String?,
) : AbstractCreatedAtResponse<UUID>(id, createdAt), PhotoPathed
