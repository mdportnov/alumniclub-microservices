package ru.mephi.alumniclub.app.model.dto.feed.response.user

import ru.mephi.alumniclub.app.model.dto.feed.response.FeedResponse
import ru.mephi.alumniclub.app.model.dto.user.response.UserShortResponse
import ru.mephi.alumniclub.shared.dto.AbstractCreatedAtResponse
import ru.mephi.alumniclub.shared.dto.photo.PhotoPathed
import java.time.LocalDateTime
import java.util.*

class EventResponseForUser(
    id: UUID,
    publicationDate: LocalDateTime,

    val title: String,
    val content: String,
    val humanUrl: String,
    var place: String? = null,
    var feed: FeedResponse,
    val author: UserShortResponse?,
    val time: LocalDateTime,
    var liked: Boolean,
    var joined: Boolean,
    var likesCount: Long,
    val viewsCount: Long,
    val tag: String,
    val externalRegistrationLink: String?,
    var registrationIsOpen: Boolean = true,

    override var photoPath: String? = null,
) : AbstractCreatedAtResponse<UUID>(id, publicationDate), PhotoPathed
