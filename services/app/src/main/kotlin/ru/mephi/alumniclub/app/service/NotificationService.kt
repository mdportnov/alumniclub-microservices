package ru.mephi.alumniclub.app.service

import io.swagger.v3.oas.annotations.Parameter
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import ru.mephi.alumniclub.app.database.entity.notification.Notification
import ru.mephi.alumniclub.app.model.dto.CursorResponse
import ru.mephi.alumniclub.app.model.dto.PageResponse
import ru.mephi.alumniclub.app.model.dto.notification.request.NotificationByPublicationRequest
import ru.mephi.alumniclub.app.model.dto.notification.response.NotificationResponse
import ru.mephi.alumniclub.app.model.dto.notification.response.NotificationShortResponse
import ru.mephi.alumniclub.app.model.dto.notification.response.UserNotificationInfoResponse
import ru.mephi.alumniclub.shared.dto.ExtendedPageRequest
import ru.mephi.alumniclub.shared.util.Cursor

interface NotificationService {
    fun list(pageRequest: ExtendedPageRequest): PageResponse<NotificationShortResponse>
    fun list(@Parameter(hidden = true) userId: Long, cursor: Cursor): CursorResponse<NotificationShortResponse>
    fun getByIdForAdmin(@PathVariable id: Long): NotificationResponse
    fun getByIdForUser(
        @Parameter(hidden = true) userId: Long,
        @PathVariable("id") notificationId: Long
    ): NotificationShortResponse

    fun deleteForUser(
        @Parameter(hidden = true) userId: Long,
        @PathVariable notificationId: Long
    )

    fun delete(@PathVariable id: Long)
    fun create(
        @RequestBody request: NotificationByPublicationRequest
    ): Notification

    fun getCountOfUnreadNotifications(userId: Long): UserNotificationInfoResponse
}
