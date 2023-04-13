package ru.mephi.alumniclub.app.database.entity.user

import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import ru.mephi.alumniclub.shared.database.entity.AbstractCreatedAtEntity
import ru.mephi.alumniclub.shared.util.constants.largeLength
import javax.persistence.*

@Entity
@Table(name = "VerifyEmailToken")
class VerifyEmailToken(
    @OnDelete(action = OnDeleteAction.CASCADE)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", nullable = false)
    @MapsId
    var user: User,

    @Column(name = "token", nullable = false, length = largeLength)
    var token: String,
) : AbstractCreatedAtEntity()
