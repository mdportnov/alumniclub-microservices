package ru.mephi.alumniclub.app.model.dto.notification.request

import ru.mephi.alumniclub.app.controller.validator.notification.NotificationTypeConstraint
import ru.mephi.alumniclub.app.model.enumeration.fcm.BroadcastType

@NotificationTypeConstraint
abstract class AbstractNotificationRequest(
    var broadcastType: BroadcastType,
    var receiversIds: List<Long>,
)
