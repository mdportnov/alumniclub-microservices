package ru.mephi.alumniclub.app.database.entity.outbox

import ru.mephi.alumniclub.shared.database.entity.AbstractCreatedAtEntity
import ru.mephi.alumniclub.shared.util.constants.rabbitMessageLength
import ru.mephi.alumniclub.shared.util.enums.RabbitMessageType
import javax.persistence.*

@Entity
@Table(name = "Outbox")
class OutboxMessage(
    @Lob
    @Column(name = "payload", nullable = false, length = rabbitMessageLength)
    var payload: String,

    @Enumerated(EnumType.STRING)
    @Column(
        name = "type", nullable = false,
        columnDefinition = "enum('MAIL_PUBLICATION_CONTENT', 'MAIL_RESET_PASSWORD', 'MAIL_VERIFY_EMAIL', " +
                "'MAIL_BROADCAST', 'PUBLICATIONS_EVENT_QUEUE', 'PUBLICATIONS_USERS_FEEDBACK_QUEUE', " +
                "'CONTENT_PHOTO_QUEUE', 'IMAGE_THUMBNAIL_QUEUE')"
    )
    var type: RabbitMessageType,

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, columnDefinition = "enum('PENDING', 'ERRORED', 'DELIVERED')")
    var status: OutboxMessageStatus = OutboxMessageStatus.PENDING
): AbstractCreatedAtEntity()