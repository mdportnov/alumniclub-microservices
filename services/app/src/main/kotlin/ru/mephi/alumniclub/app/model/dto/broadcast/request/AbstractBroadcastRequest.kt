package ru.mephi.alumniclub.app.model.dto.broadcast.request

import ru.mephi.alumniclub.app.controller.validator.notification.NotificationTypeConstraint
import ru.mephi.alumniclub.app.model.dto.broadcast.BroadcastOptionsDTO
import ru.mephi.alumniclub.app.model.dto.notification.request.AbstractNotificationRequest
import ru.mephi.alumniclub.app.model.enumeration.fcm.BroadcastType

@NotificationTypeConstraint
open class AbstractBroadcastRequest(
    broadcastType: BroadcastType,
    receiversIds: List<Long>,
    val options: BroadcastOptionsDTO,
) : AbstractNotificationRequest(
    broadcastType = broadcastType,
    receiversIds = receiversIds
)
