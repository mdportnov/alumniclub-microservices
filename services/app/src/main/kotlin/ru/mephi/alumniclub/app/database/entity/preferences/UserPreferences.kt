package ru.mephi.alumniclub.app.database.entity.preferences

import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import ru.mephi.alumniclub.app.database.entity.user.User
import ru.mephi.alumniclub.shared.database.entity.AbstractEntity
import javax.persistence.*

@Entity
@Table(name = "UserPreferences")
class UserPreferences(
    @OnDelete(action = OnDeleteAction.CASCADE)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", nullable = false)
    @MapsId
    var user: User? = null,

    @Column(name = "enablePush", nullable = false)
    var enablePush: Boolean = true,

    @Column(name = "enableEmail", nullable = false)
    var enableEmail: Boolean = true
) : AbstractEntity()
