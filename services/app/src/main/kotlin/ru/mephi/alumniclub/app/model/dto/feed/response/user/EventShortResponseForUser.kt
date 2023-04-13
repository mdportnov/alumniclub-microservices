package ru.mephi.alumniclub.app.model.dto.feed.response.user

import ru.mephi.alumniclub.app.model.dto.feed.response.FeedResponse
import ru.mephi.alumniclub.shared.dto.AbstractCreatedAtResponse
import ru.mephi.alumniclub.shared.dto.photo.PhotoPathed
import java.time.LocalDateTime
import java.util.*

open class EventShortResponseForUser(
    id: UUID,
    publicationDate: LocalDateTime,

    val title: String,
    val humanUrl: String,
    var place: String? = null,
    var feed: FeedResponse,
    var content: String = "",
    var liked: Boolean,
    var joined: Boolean,
    val viewsCount: Long,
    val likesCount: Long = 0,
    val time: LocalDateTime,
    val tag: String,
    val externalRegistrationLink: String?,
    var registrationIsOpen: Boolean = true,

    override var photoPath: String? = null,
) : AbstractCreatedAtResponse<UUID>(id, publicationDate), PhotoPathed
