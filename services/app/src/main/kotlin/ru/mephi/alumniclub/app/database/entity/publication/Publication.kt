package ru.mephi.alumniclub.app.database.entity.publication

import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import ru.mephi.alumniclub.app.database.entity.feed.PublicationFeed
import ru.mephi.alumniclub.app.model.enumeration.NotificationCategory
import ru.mephi.alumniclub.shared.util.constants.largeLength
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "Publication")
class Publication(
    title: String,
    content: String,

    /**
    Substituted as createdAt in user responses. If later than the current date,
    the user will not be able to receive the publication
     */
    @Column(nullable = false)
    var publicationDate: LocalDateTime,

    @Column(name = "humanUrl", nullable = false, length = largeLength, unique = true)
    var humanUrl: String,

    @Column(name = "isHidden", nullable = false)
    var hidden: Boolean = false

) : AbstractPublication(
    title, content
) {
    @OnDelete(action = OnDeleteAction.CASCADE)
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "feedId")
    override lateinit var feed: PublicationFeed

    override val category: NotificationCategory
        get() = feed.category
}
