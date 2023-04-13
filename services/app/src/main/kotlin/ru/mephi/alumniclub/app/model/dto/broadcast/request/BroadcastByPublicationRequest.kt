package ru.mephi.alumniclub.app.model.dto.broadcast.request

import ru.mephi.alumniclub.app.database.entity.publication.AbstractPublication
import ru.mephi.alumniclub.app.model.dto.broadcast.BroadcastOptionsDTO
import ru.mephi.alumniclub.app.model.enumeration.fcm.BroadcastType


class BroadcastByPublicationRequest(
    broadcastType: BroadcastType,
    receiversIds: List<Long>,
    options: BroadcastOptionsDTO,
    val publication: AbstractPublication
) : AbstractBroadcastRequest(broadcastType, receiversIds, options)
