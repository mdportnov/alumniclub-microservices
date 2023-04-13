package ru.mephi.alumniclub.app.database.entity.notification

import org.hibernate.annotations.ColumnDefault
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import ru.mephi.alumniclub.app.database.entity.user.User
import ru.mephi.alumniclub.shared.database.entity.AbstractEntity
import javax.persistence.*

@Entity
@Table(name = "UserNotification")
class UserNotification(
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "notificationId", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    var notification: Notification,

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "userId", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    var user: User,

    @ColumnDefault("false")
    @Column(name = "isRead", nullable = false)
    var isRead: Boolean = false
) : AbstractEntity()
