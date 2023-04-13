package ru.mephi.alumniclub.app.database.entity.fcm

import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import ru.mephi.alumniclub.app.database.entity.user.Fingerprint
import ru.mephi.alumniclub.shared.database.entity.AbstractCreatedAtEntity
import ru.mephi.alumniclub.shared.util.constants.largeLength
import javax.persistence.*


@Entity
@Table(name = "FcmToken")
class FcmToken(
    @MapsId
    @OnDelete(action = OnDeleteAction.CASCADE)
    @OneToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "id", nullable = false)
    var fingerprint: Fingerprint,

    @Column(name = "token", nullable = false, length = largeLength)
    var token: String,

    @Column(name = "isValid", nullable = false)
    var isValid: Boolean
) : AbstractCreatedAtEntity()
