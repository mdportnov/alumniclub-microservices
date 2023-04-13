package ru.mephi.alumniclub.app.database.entity.feed

import javax.persistence.DiscriminatorValue
import javax.persistence.Entity

@Entity
@DiscriminatorValue(EventFeed.DISCRIMINATOR_VALUE)
class EventFeed(
    name: String
) : AbstractFeed(name) {
    companion object {
        const val DISCRIMINATOR_VALUE = "EVENT"
    }
}
