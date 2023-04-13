package ru.mephi.alumniclub.app.model.dto.notification.response

import ru.mephi.alumniclub.app.model.enumeration.NotificationCategory
import ru.mephi.alumniclub.shared.dto.AbstractCreatedAtResponse
import java.time.LocalDateTime
import java.util.*

open class NotificationShortResponse(
    id: Long,
    createdAt: LocalDateTime,
    var isRead: Boolean = false,

    var title: String? = null,
    var feedName: String? = null,
    var feedId: Long? = null,
    var publicationId: UUID? = null,
    var category: NotificationCategory? = null
) : AbstractCreatedAtResponse<Long>(id, createdAt)
