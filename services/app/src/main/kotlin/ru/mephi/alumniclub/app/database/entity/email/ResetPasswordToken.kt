package ru.mephi.alumniclub.app.database.entity.email

import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import ru.mephi.alumniclub.app.database.entity.user.User
import ru.mephi.alumniclub.shared.database.entity.AbstractCreatedAtEntity
import javax.persistence.*

@Entity
@Table(name = "ResetPasswordToken")
class ResetPasswordToken(
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @MapsId
    var user: User,

    @Column(name = "token", nullable = false, length = 36)
    var token: String
) : AbstractCreatedAtEntity()
