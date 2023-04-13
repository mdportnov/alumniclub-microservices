package ru.mephi.alumniclub.app.model.mapper.notifications

import org.springframework.context.annotation.Lazy
import org.springframework.data.domain.Page
import org.springframework.stereotype.Component
import ru.mephi.alumniclub.app.database.entity.notification.Notification
import ru.mephi.alumniclub.app.database.entity.notification.UserNotification
import ru.mephi.alumniclub.app.model.dto.CursorResponse
import ru.mephi.alumniclub.app.model.dto.PageResponse
import ru.mephi.alumniclub.app.model.dto.broadcast.request.AbstractBroadcastRequest
import ru.mephi.alumniclub.app.model.dto.notification.request.AbstractNotificationRequest
import ru.mephi.alumniclub.app.model.dto.notification.response.NotificationCursorResponse
import ru.mephi.alumniclub.app.model.dto.notification.response.NotificationResponse
import ru.mephi.alumniclub.app.model.dto.notification.response.NotificationShortResponse
import ru.mephi.alumniclub.app.model.dto.notification.response.UserNotificationInfoResponse
import ru.mephi.alumniclub.app.service.FullPublicationService
import java.util.*

@Component
class NotificationMapper(
    @Lazy private val helper: FullPublicationService
) {
    fun asEntity(request: AbstractNotificationRequest, publicationId: UUID): Notification {
        return Notification(
            broadcastType = request.broadcastType,
            receiversIds = request.receiversIds.toMutableList(),
            publicationId = publicationId
        )
    }

    fun asEntity(request: AbstractBroadcastRequest): Notification {
        return Notification(
            broadcastType = request.broadcastType,
            receiversIds = request.receiversIds.toMutableList(),
            publicationId = UUID.randomUUID()
        )
    }

    fun asShortResponse(notification: Notification): NotificationShortResponse {
        val holder = helper.getNotificationHolder(notification.publicationId)
        return NotificationShortResponse(
            id = notification.id,
            createdAt = notification.createdAt,
            title = holder.title,
            feedName = holder.feed?.name,
            feedId = holder.feed?.id,
            publicationId = notification.publicationId,
            category = holder.category
        )
    }

    fun asResponse(notification: Notification): NotificationResponse {
        val holder = helper.getNotificationHolder(notification.publicationId)
        return NotificationResponse(
            id = notification.id,
            createdAt = notification.createdAt,
            isRead = false,
            title = holder.title,
            feedName = holder.feed?.name,
            feedId = holder.feed?.id,
            publicationId = notification.publicationId,
            category = holder.category,
            broadcastType = notification.broadcastType,
            receiversIds = notification.receiversIds
        )
    }

    fun asCursorResponse(
        page: Page<UserNotification>,
        responses: List<NotificationShortResponse>,
        info: UserNotificationInfoResponse
    ): NotificationCursorResponse<NotificationShortResponse> {
        return NotificationCursorResponse(
            content = responses,
            numberOfElements = page.numberOfElements.toLong(),
            info = info
        )
    }

    fun asPageResponse(
        page: Page<Notification>,
        notifications: List<Notification>
    ): PageResponse<NotificationShortResponse> {
        val content = notifications.map { asShortResponse(it) }
        return PageResponse(
            content = content,
            number = page.number.toLong(),
            numberOfElements = notifications.size.toLong(),
            totalPages = page.totalPages.toLong()
        )
    }

    fun asShortCursorWithRead(
        page: Page<UserNotification>,
        userNotifications: List<UserNotification>,
        info: UserNotificationInfoResponse
    ): CursorResponse<NotificationShortResponse> {
        val result = userNotifications.map {
            val response = asShortResponse(it.notification)
            response.isRead = it.isRead
            response
        }
        return asCursorResponse(page, result, info)
    }
}
