package ru.mephi.alumniclub.app.database.entity.fcm

import org.hibernate.annotations.Type
import ru.mephi.alumniclub.app.model.enumeration.fcm.BroadcastType
import ru.mephi.alumniclub.app.model.enumeration.fcm.PushStatus
import ru.mephi.alumniclub.shared.database.entity.AbstractCreatedAtEntity
import ru.mephi.alumniclub.shared.util.constants.largeLength
import ru.mephi.alumniclub.shared.util.constants.smallLength
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "PushNotification")
class PushNotification(
    @Column(name = "title", nullable = false, length = smallLength)
    var title: String,

    @Column(name = "text", nullable = false, length = largeLength)
    var text: String,

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, columnDefinition = "enum( 'USERS', 'COMMUNITIES', 'ALL' )")
    var broadcastType: BroadcastType,

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, columnDefinition = "enum( 'SENDED', 'ERRORED', 'PENDING' )")
    var status: PushStatus,

    @Type(type = "uuid-char")
    @Column(name = "contentId", nullable = true)
    var contentId: UUID? = null,

    @ElementCollection
    @CollectionTable(
        name = "PushNotificationReceivers",
        joinColumns = [JoinColumn(
            name = "pushId", foreignKey = ForeignKey(
                foreignKeyDefinition = "foreign key (pushId) references PushNotification (id) on delete cascade"
            ),
            insertable = false, updatable = false
        )]
    )
    @Column(name = "receiverId", nullable = false)
    var receiversIds: MutableList<Long> = mutableListOf(),


    @Column(name = "erroredTokens", nullable = false)
    @OneToMany(
        mappedBy = "push", fetch = FetchType.EAGER,
        cascade = [CascadeType.ALL], orphanRemoval = true
    )
    var erroredTokens: MutableList<ErroredFcmToken> = mutableListOf()
) : AbstractCreatedAtEntity()
