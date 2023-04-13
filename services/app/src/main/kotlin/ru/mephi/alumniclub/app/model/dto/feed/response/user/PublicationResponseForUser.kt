package ru.mephi.alumniclub.app.model.dto.feed.response.user

import ru.mephi.alumniclub.app.model.dto.feed.response.FeedResponse
import ru.mephi.alumniclub.app.model.dto.user.response.UserShortResponse
import ru.mephi.alumniclub.shared.dto.AbstractCreatedAtResponse
import ru.mephi.alumniclub.shared.dto.photo.PhotoPathed
import java.time.LocalDateTime
import java.util.*

open class PublicationResponseForUser(
    id: UUID,
    publicationDate: LocalDateTime,

    val title: String,
    val content: String,
    val author: UserShortResponse?,
    var feed: FeedResponse,
    var liked: Boolean,
    val humanUrl: String,
    var likesCount: Long = -1,
    val viewsCount: Long,

    override var photoPath: String? = null,
) : AbstractCreatedAtResponse<UUID>(id, publicationDate), PhotoPathed
