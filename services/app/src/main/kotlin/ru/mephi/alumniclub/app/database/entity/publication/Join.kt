package ru.mephi.alumniclub.app.database.entity.publication

import org.hibernate.annotations.Type
import ru.mephi.alumniclub.app.database.entity.feed.MeetingParticipation
import ru.mephi.alumniclub.app.database.entity.user.User
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "EventJoin")
class Join(
    user: User,

    @Type(type = "uuid-char")
    @Column(nullable = false)
    val eventId: UUID,

    @PrimaryKeyJoinColumn
    @OneToOne(mappedBy = "join", cascade = [CascadeType.ALL], orphanRemoval = true)
    var meetingParticipation: MeetingParticipation? = null
) : AbstractReaction(user)
