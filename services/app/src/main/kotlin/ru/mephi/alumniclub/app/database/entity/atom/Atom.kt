package ru.mephi.alumniclub.app.database.entity.atom

import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import org.hibernate.annotations.Type
import ru.mephi.alumniclub.app.database.entity.feed.AbstractFeed
import ru.mephi.alumniclub.app.database.entity.notification.NotificationHolder
import ru.mephi.alumniclub.app.database.entity.user.User
import ru.mephi.alumniclub.app.model.enumeration.NotificationCategory
import ru.mephi.alumniclub.shared.util.constants.mediumLength
import java.time.LocalDateTime
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "Atom")
class Atom(
    @Column(name = "description", nullable = false, length = mediumLength)
    var description: String,

    @Column(name = "amount", nullable = false)
    var amount: Int,

    @Column(name = "sign", nullable = false)
    var sign: Boolean,

    @OnDelete(action = OnDeleteAction.CASCADE)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "userId", nullable = false)
    var user: User
) : NotificationHolder {
    @Id
    @Type(type = "uuid-char")
    @Column(nullable = false, unique = true)
    override var id: UUID = UUID.randomUUID()

    @Column(name = "createdAt", nullable = false)
    var createdAt: LocalDateTime = LocalDateTime.now()

    init {
        id = UUID.randomUUID()
        createdAt = LocalDateTime.now()
    }

    override val category: NotificationCategory
        get() = NotificationCategory.ATOMS
    override val title: String
        get() {
            return if (sign) {
                "На Ваш счёт начислили $amount атомов! Причина: $description"
            } else {
                "С Вашего счёта списали $amount атомов! Причина: $description"
            }
        }
    override val feed: AbstractFeed?
        get() = null
}
