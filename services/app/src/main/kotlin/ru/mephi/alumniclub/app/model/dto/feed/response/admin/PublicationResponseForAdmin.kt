package ru.mephi.alumniclub.app.model.dto.feed.response.admin

import ru.mephi.alumniclub.app.model.dto.feed.response.FeedResponse
import ru.mephi.alumniclub.app.model.dto.user.response.UserShortResponse
import ru.mephi.alumniclub.shared.dto.AbstractCreatedAtResponse
import ru.mephi.alumniclub.shared.dto.photo.PhotoPathed
import java.time.LocalDateTime
import java.util.*

class PublicationResponseForAdmin(
    id: UUID,
    createdAt: LocalDateTime,

    val publicationDate: LocalDateTime,
    val title: String,
    val content: String,
    val author: UserShortResponse?,
    var feed: FeedResponse,
    val humanUrl: String,
    var likesCount: Long,
    val viewsCount: Long,
    var hidden: Boolean,

    override var photoPath: String? = null,
) : AbstractCreatedAtResponse<UUID>(id, createdAt), PhotoPathed
