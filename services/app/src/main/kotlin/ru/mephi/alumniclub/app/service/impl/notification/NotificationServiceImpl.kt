package ru.mephi.alumniclub.app.service.impl.notification

import org.springframework.context.annotation.Lazy
import org.springframework.data.jpa.repository.Modifying
import org.springframework.stereotype.Service
import ru.mephi.alumniclub.app.database.entity.atom.Atom
import ru.mephi.alumniclub.app.database.entity.notification.Notification
import ru.mephi.alumniclub.app.database.entity.notification.NotificationHolder
import ru.mephi.alumniclub.app.database.entity.notification.UserNotification
import ru.mephi.alumniclub.app.database.entity.publication.Event
import ru.mephi.alumniclub.app.database.entity.publication.Publication
import ru.mephi.alumniclub.app.database.entity.survey.Survey
import ru.mephi.alumniclub.app.database.repository.notification.NotificationDao
import ru.mephi.alumniclub.app.database.repository.notification.UserNotificationDao
import ru.mephi.alumniclub.app.model.dto.CursorResponse
import ru.mephi.alumniclub.app.model.dto.PageResponse
import ru.mephi.alumniclub.app.model.dto.notification.request.AbstractNotificationRequest
import ru.mephi.alumniclub.app.model.dto.notification.request.NotificationByPublicationRequest
import ru.mephi.alumniclub.app.model.dto.notification.response.NotificationResponse
import ru.mephi.alumniclub.app.model.dto.notification.response.NotificationShortResponse
import ru.mephi.alumniclub.app.model.dto.notification.response.UserNotificationInfoResponse
import ru.mephi.alumniclub.app.model.exception.common.ResourceNotFoundException
import ru.mephi.alumniclub.app.model.mapper.notifications.NotificationMapper
import ru.mephi.alumniclub.app.service.DataSenderHelper
import ru.mephi.alumniclub.app.service.FullPublicationService
import ru.mephi.alumniclub.app.service.NotificationService
import ru.mephi.alumniclub.shared.dto.ExtendedPageRequest
import ru.mephi.alumniclub.shared.util.Cursor
import ru.mephi.alumniclub.shared.util.response.ResponseManager
import javax.transaction.Transactional

@Service
@Transactional
class NotificationServiceImpl(
    private val notificationDao: NotificationDao,
    private val userNotificationDao: UserNotificationDao,
    private val mapper: NotificationMapper,
    private val dataSenderHelper: DataSenderHelper,
    @Lazy
    private val fullPublicationService: FullPublicationService
) : ResponseManager(), NotificationService {

    private fun findEntityById(id: Long): Notification {
        return notificationDao.findById(id)
            .orElseThrow { ResourceNotFoundException(Notification::class.java, id = id) }
    }

    private fun broadcast(request: AbstractNotificationRequest, notification: Notification) {
        dataSenderHelper.validateIdsOrThrow(request.broadcastType, request.receiversIds)
        dataSenderHelper.getReceiversUsers(request.broadcastType, request.receiversIds).forEach { user ->
            userNotificationDao.save(UserNotification(notification = notification, user = user))
        }
    }

    /**
     * Filters notifications by the existence predicate of the content to which the notification is bound.
     *
     * All notifications are tied to some content [NotificationHolder].
     * For example [Publication], [Event], [Survey], [Atom] ...
     * If the content to which the notification is attached does not exist,
     *      this notification will be deleted from the database and will not be returned from the method.
     *
     * @param list The [List] of [Notification] to filtering
     * @return The [List] containing only relevant [Notification]
     */
    private fun filterActualNotifications(list: List<Notification>): List<Notification> {
        val result = mutableListOf<Notification>()
        for (notification in list) {
            if (fullPublicationService.existsNotificationHolder(notification.publicationId))
                result.add(notification)
            else
                delete(notification.id)
        }
        return result
    }

    private fun filterActualNotifications(notification: Notification): List<Notification> {
        return filterActualNotifications(listOf(notification))
    }

    @Transactional
    override fun list(pageRequest: ExtendedPageRequest): PageResponse<NotificationShortResponse> {
        val page = notificationDao.findAll(pageRequest.pageable)
        val notifications = filterActualNotifications(page.content)
        return mapper.asPageResponse(page, notifications)
    }

    /**
     * Returns a paginated list of [NotificationShortResponse] to user from the database filtered by chronology
     *
     * @param userId The ID of the user for whom to retrieve the notifications.
     * @param cursor The [Cursor] used to filter and paginate the list of notifications.
     */
    @Modifying
    @Transactional
    override fun list(userId: Long, cursor: Cursor): CursorResponse<NotificationShortResponse> {
        val page = when (cursor.chronology) {
            Cursor.Chronology.BEFORE -> notificationDao.findByCreatedAtBeforeAndUserId(userId, cursor.from, cursor.page)
            Cursor.Chronology.AFTER -> notificationDao.findByCreatedAtAfterAndUserId(userId, cursor.from, cursor.page)
        }
        val notifications = filterActualNotifications(page.content.map { it.notification })
        val userNotifications = page.content.filter { notifications.contains(it.notification) }
        val info = getCountOfUnreadNotifications(userId)
        val responses = mapper.asShortCursorWithRead(page, userNotifications, info)
        userNotifications.forEach { it.isRead = true }
        return responses
    }

    override fun getByIdForAdmin(id: Long): NotificationResponse {
        val notification = filterActualNotifications(findEntityById(id))
        if (notification.isEmpty()) throw ResourceNotFoundException(Notification::class.java, id)
        return mapper.asResponse(notification.first())
    }

    override fun getByIdForUser(userId: Long, notificationId: Long): NotificationShortResponse {
        if (!userNotificationDao.existsByUserIdAndNotificationId(userId, notificationId))
            throw ResourceNotFoundException(Notification::class.java, notificationId)
        val notification = filterActualNotifications(findEntityById(notificationId))
        if (notification.isEmpty()) throw ResourceNotFoundException(Notification::class.java, notificationId)
        return mapper.asShortResponse(notification.first())
    }

    override fun create(request: NotificationByPublicationRequest): Notification {
        val entity = mapper.asEntity(request, request.publicationId)
        val notification = notificationDao.save(entity)
        broadcast(request, notification)
        return notification
    }

    override fun getCountOfUnreadNotifications(userId: Long): UserNotificationInfoResponse {
        return UserNotificationInfoResponse(userNotificationDao.countByUserIdAndIsReadIs(userId, false))
    }

    @Modifying
    override fun deleteForUser(userId: Long, notificationId: Long) {
        if (!userNotificationDao.existsByUserIdAndNotificationId(userId, notificationId))
            throw ResourceNotFoundException(Notification::class.java, notificationId)
        userNotificationDao.deleteByUserIdAndNotificationId(userId, notificationId)
    }

    @Modifying
    override fun delete(id: Long) {
        val notification = findEntityById(id)
        notificationDao.deleteById(notification.id)
    }
}
