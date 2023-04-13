package ru.mephi.alumniclub.app.model.dto.feed.response.admin

import ru.mephi.alumniclub.app.model.dto.feed.response.FeedResponse
import ru.mephi.alumniclub.shared.dto.AbstractCreatedAtResponse
import ru.mephi.alumniclub.shared.dto.photo.PhotoPathed
import java.time.LocalDateTime
import java.util.*

open class EventShortResponseForAdmin(
    id: UUID,
    createdAt: LocalDateTime,

    val publicationDate: LocalDateTime,
    val title: String,
    val humanUrl: String,
    var place: String? = null,
    var feed: FeedResponse,
    var content: String = "",
    var likesCount: Long,
    val viewsCount: Long,
    var joinsCount: Long,
    val time: LocalDateTime,
    val tag: String,
    val externalRegistrationLink: String?,
    var hidden: Boolean,
    var registrationIsOpen: Boolean = true,

    override var photoPath: String? = null,
) : AbstractCreatedAtResponse<UUID>(id, createdAt), PhotoPathed
