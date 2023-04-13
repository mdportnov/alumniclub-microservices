package ru.mephi.alumniclub.app.model.dto.notification.request

import ru.mephi.alumniclub.app.model.enumeration.NotificationCategory
import ru.mephi.alumniclub.app.model.enumeration.fcm.BroadcastType

class NotificationRequest(
    val title: String,
    val category: NotificationCategory,
    broadcastType: BroadcastType,
    receiversIds: List<Long>
) : AbstractNotificationRequest(broadcastType, receiversIds)
