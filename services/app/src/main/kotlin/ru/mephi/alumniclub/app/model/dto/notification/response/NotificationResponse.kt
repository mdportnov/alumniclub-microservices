package ru.mephi.alumniclub.app.model.dto.notification.response

import ru.mephi.alumniclub.app.model.enumeration.NotificationCategory
import ru.mephi.alumniclub.app.model.enumeration.fcm.BroadcastType
import java.time.LocalDateTime
import java.util.*

class NotificationResponse(
    id: Long,
    createdAt: LocalDateTime,
    isRead: Boolean = false,
    title: String? = null,
    feedName: String?,
    feedId: Long?,
    publicationId: UUID? = null,
    category: NotificationCategory? = null,

    val broadcastType: BroadcastType,
    val receiversIds: List<Long>,
) : NotificationShortResponse(id, createdAt, isRead, title, feedName, feedId, publicationId, category)
