package ru.mephi.alumniclub.app.database.entity.notification

import org.hibernate.annotations.Type
import ru.mephi.alumniclub.app.model.enumeration.fcm.BroadcastType
import ru.mephi.alumniclub.shared.database.entity.AbstractCreatedAtEntity
import java.util.*
import javax.persistence.*


@Entity
@Table(name = "Notification")
class Notification(
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "enum('USERS', 'COMMUNITIES', 'ALL')")
    var broadcastType: BroadcastType,

    @ElementCollection
    @CollectionTable(
        name = "NotificationReceivers",
        joinColumns = [JoinColumn(
            name = "notificationId", foreignKey = ForeignKey(
                foreignKeyDefinition = "foreign key (notificationId) references Notification (id) on delete cascade"
            ),
            insertable = false, updatable = false
        )]
    )
    @Column(name = "receiverId", nullable = false)
    var receiversIds: MutableList<Long> = mutableListOf(),

    @Type(type = "uuid-char")
    @Column(nullable = false)
    var publicationId: UUID
) : AbstractCreatedAtEntity()
