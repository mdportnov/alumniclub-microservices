package ru.mephi.alumniclub.app.database.repository.notification

import org.springframework.stereotype.Repository
import ru.mephi.alumniclub.app.database.entity.notification.UserNotification
import ru.mephi.alumniclub.shared.database.repository.AbstractRepository

@Repository
interface UserNotificationDao : AbstractRepository<UserNotification> {
    fun existsByUserIdAndNotificationId(userId: Long, notificationId: Long): Boolean
    fun deleteByUserIdAndNotificationId(userId: Long, notificationId: Long)
    fun countByUserIdAndIsReadIs(userId: Long, isRead: Boolean): Int
}
