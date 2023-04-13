package ru.mephi.alumniclub.app.database.entity.broadcast

import ru.mephi.alumniclub.app.database.entity.feed.AbstractFeed
import ru.mephi.alumniclub.app.database.entity.publication.AbstractPublication
import ru.mephi.alumniclub.app.model.enumeration.NotificationCategory
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(name = "Broadcast")
class Broadcast(
    title: String,
    content: String,
) : AbstractPublication(title, content) {
    override val category: NotificationCategory
        get() = NotificationCategory.BROADCASTS
    override val feed: AbstractFeed?
        get() = null
}
