package ru.mephi.alumniclub.app.model.dto.fcm.response

import ru.mephi.alumniclub.app.model.enumeration.fcm.BroadcastType
import ru.mephi.alumniclub.app.model.enumeration.fcm.PushStatus
import java.time.LocalDateTime

class PushNotificationResponse(
    var id: Long,
    var createdAt: LocalDateTime = LocalDateTime.now(),
    var title: String = "",
    var text: String = "",
    var type: BroadcastType? = null,
    var status: PushStatus? = null,
    var receiversIds: MutableList<Long> = ArrayList(),
)
