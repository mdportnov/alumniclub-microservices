package ru.mephi.alumniclub.app.database.entity.fcm

import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import ru.mephi.alumniclub.app.model.enumeration.fcm.PushError
import ru.mephi.alumniclub.shared.database.entity.AbstractCreatedAtEntity
import javax.persistence.*

@Entity
@Table(name = "ErroredFcmToken")
class ErroredFcmToken(
    @OnDelete(action = OnDeleteAction.CASCADE)
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "pushId", nullable = false)
    var push: PushNotification,

    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "tokenId", nullable = false)
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    var token: FcmToken,

    @Enumerated(EnumType.STRING)
    @Column(
        name = "pushError", nullable = false,
        columnDefinition = "enum( 'THIRD_PARTY_AUTH_ERROR', 'INVALID_ARGUMENT', 'INTERNAL', 'QUOTA_EXCEEDED', " +
                "'SENDER_ID_MISMATCH', 'UNAVAILABLE', 'INVALID_TOKEN' )"
    )
    var error: PushError,
) : AbstractCreatedAtEntity()
