package ru.mephi.alumniclub.app.model.dto.notification.request

import ru.mephi.alumniclub.app.model.enumeration.fcm.BroadcastType
import java.util.*

open class NotificationByPublicationRequest(
    val publicationId: UUID,
    broadcastType: BroadcastType,
    receiversIds: List<Long>,
) : AbstractNotificationRequest(
    broadcastType = broadcastType,
    receiversIds = receiversIds,
)
