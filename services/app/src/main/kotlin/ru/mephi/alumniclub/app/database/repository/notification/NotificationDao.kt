package ru.mephi.alumniclub.app.database.repository.notification

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import ru.mephi.alumniclub.app.database.entity.notification.Notification
import ru.mephi.alumniclub.app.database.entity.notification.UserNotification
import ru.mephi.alumniclub.shared.database.repository.AbstractRepository
import java.time.LocalDateTime

@Repository
interface NotificationDao : AbstractRepository<Notification>, PagingAndSortingRepository<Notification, Long> {
    fun deleteAllByCreatedAtBefore(before: LocalDateTime)

    @Query(
        value = """
        SELECT un
        FROM Notification notif
        INNER JOIN UserNotification un ON un.notification.id = notif.id
        WHERE un.user.id = :user_id and notif.createdAt < :from
        """
    )
    fun findByCreatedAtBeforeAndUserId(
        @Param("user_id") userId: Long,
        @Param("from") from: LocalDateTime,
        pageable: Pageable,
    ): Page<UserNotification>

    @Query(
        value = """
        SELECT un
        FROM Notification notif
        INNER JOIN UserNotification un ON un.notification.id = notif.id
        WHERE un.user.id = :user_id and notif.createdAt > :from
        """
    )
    fun findByCreatedAtAfterAndUserId(
        @Param("user_id") userId: Long,
        @Param("from") from: LocalDateTime,
        pageable: Pageable,
    ): Page<UserNotification>

    @Query(
        value = """
        SELECT DISTINCT notif
        FROM Notification notif
        INNER JOIN UserNotification un ON un.notification.id = notif.id
        WHERE un.user.id = :user_id
        """
    )
    fun findByUserId(
        @Param("user_id") userId: Long,
        pageable: Pageable,
    ): Page<Notification>
}
