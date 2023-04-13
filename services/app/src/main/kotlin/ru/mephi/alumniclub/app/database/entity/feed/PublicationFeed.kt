package ru.mephi.alumniclub.app.database.entity.feed

import ru.mephi.alumniclub.app.model.enumeration.NotificationCategory
import javax.persistence.*

@Entity
@DiscriminatorValue(PublicationFeed.DISCRIMINATOR_VALUE)
class PublicationFeed(
    name: String,

    @Enumerated(EnumType.STRING)
    @Column(
        nullable = false,
        columnDefinition = "enum( 'EVENTS', 'PROJECTS', 'ENDOWMENTS', 'PARTNERSHIPS', 'ACHIEVEMENTS', 'SURVEYS', 'ATOMS', 'BROADCAST' )"
    )
    val category: NotificationCategory

) : AbstractFeed(name) {
    companion object {
        const val DISCRIMINATOR_VALUE = "PUBLICATION"
    }
}
