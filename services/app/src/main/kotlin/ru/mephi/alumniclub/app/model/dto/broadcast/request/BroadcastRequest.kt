package ru.mephi.alumniclub.app.model.dto.broadcast.request

import ru.mephi.alumniclub.app.model.dto.broadcast.BroadcastOptionsDTO
import ru.mephi.alumniclub.app.model.enumeration.fcm.BroadcastType
import javax.validation.constraints.Size

open class BroadcastRequest(
    @field:Size(max = 300)
    var title: String,
    @field:Size(max = 60000)
    val content: String,
    broadcastType: BroadcastType,
    receiversIds: List<Long>,
    options: BroadcastOptionsDTO = BroadcastOptionsDTO()
) : AbstractBroadcastRequest(
    broadcastType = broadcastType,
    receiversIds = receiversIds,
    options = options,
)