package ru.mephi.alumniclub.app.model.mapper.fcm

import org.springframework.stereotype.Component
import ru.mephi.alumniclub.app.database.entity.fcm.ErroredFcmToken
import ru.mephi.alumniclub.app.database.entity.fcm.FcmToken
import ru.mephi.alumniclub.app.database.entity.fcm.PushNotification
import ru.mephi.alumniclub.app.database.entity.notification.NotificationHolder
import ru.mephi.alumniclub.app.database.entity.publication.AbstractPublication
import ru.mephi.alumniclub.app.database.entity.publication.Event
import ru.mephi.alumniclub.app.database.entity.publication.Publication
import ru.mephi.alumniclub.app.model.dto.broadcast.BroadcastOptionsDTO
import ru.mephi.alumniclub.app.model.dto.broadcast.request.BroadcastByPublicationRequest
import ru.mephi.alumniclub.app.model.dto.broadcast.request.BroadcastRequest
import ru.mephi.alumniclub.app.model.enumeration.fcm.BroadcastType
import ru.mephi.alumniclub.app.model.enumeration.fcm.PushStatus
import java.util.*

@Component
class PushNotificationMapper {
    fun asRequest(entity: PushNotification): BroadcastRequest {
        return BroadcastRequest(
            title = entity.title,
            content = entity.text,
            broadcastType = entity.broadcastType,
            receiversIds = if (entity.broadcastType == BroadcastType.ALL) emptyList() else entity.receiversIds,
            options = BroadcastOptionsDTO()
        )
    }

    fun asEntity(request: BroadcastRequest, contentId: UUID? = null): PushNotification {
        val receiversIds = if (request.broadcastType == BroadcastType.ALL) mutableListOf()
        else request.receiversIds.toMutableList()
        return PushNotification(
            title = request.title,
            text = request.content,
            contentId = contentId,
            broadcastType = request.broadcastType,
            receiversIds = receiversIds,
            status = PushStatus.PENDING
        )
    }

    fun asBroadcastForPush(request: BroadcastByPublicationRequest): BroadcastRequest {
        return BroadcastRequest(
            broadcastType = request.broadcastType,
            receiversIds = request.receiversIds,
            options = request.options,
            title = request.publication.category.title,
            content = request.publication.title
        )
    }

    fun buildExtraDataMap(holder: NotificationHolder?): Map<String, String> {
        if (holder == null) return emptyMap()
        if (holder !is AbstractPublication) return emptyMap()
        return buildExtraDataMap(holder as AbstractPublication)
    }

    fun buildExtraDataMap(publication: AbstractPublication): Map<String, String> {
        val data = mutableMapOf<String, String>()
        data["publicationId"] = publication.id.toString()
        data["category"] = publication.category.toString()
        if (publication is Publication) data["feedId"] = publication.feed.id.toString()
        if (publication is Event) data["feedId"] = publication.feed.id.toString()
        return data
    }

    fun asTokens(tokens: List<FcmToken>): List<String> {
        return tokens.map { it.token }
    }

    fun getTokens(tokens: List<ErroredFcmToken>): List<String> {
        return tokens.map { it.token.token }
    }
}
