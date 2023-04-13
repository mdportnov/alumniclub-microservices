package ru.mephi.alumniclub.app.database.entity.publication

import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import ru.mephi.alumniclub.app.database.entity.feed.EventFeed
import ru.mephi.alumniclub.app.database.entity.user.User
import ru.mephi.alumniclub.app.model.enumeration.NotificationCategory
import ru.mephi.alumniclub.shared.util.constants.largeLength
import ru.mephi.alumniclub.shared.util.constants.smallLength
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "Event")
class Event(
    title: String,
    content: String,

    @Column(nullable = false, length = largeLength, unique = true)
    var humanUrl: String,

    @Column(nullable = false)
    var time: LocalDateTime,

    @Column(length = largeLength)
    var place: String? = null,

    @Column(nullable = false)
    var publicationDate: LocalDateTime,

    @Column(length = largeLength)
    var externalRegistrationLink: String? = null,

    @Column(nullable = false, length = smallLength)
    var tag: String = "",

    @Column(name = "isHidden", nullable = false)
    var hidden: Boolean = false,

    @Column(name = "registrationIsOpen", nullable = false)
    var registrationIsOpen: Boolean = true

) : AbstractPublication(title, content) {

    @OnDelete(action = OnDeleteAction.CASCADE)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "feedId")
    override lateinit var feed: EventFeed

    override val category: NotificationCategory
        get() = NotificationCategory.EVENTS
}
