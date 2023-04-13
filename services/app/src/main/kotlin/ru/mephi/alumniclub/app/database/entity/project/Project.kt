package ru.mephi.alumniclub.app.database.entity.project

import ru.mephi.alumniclub.app.database.entity.feed.EventFeed
import javax.persistence.*

@Entity
@DiscriminatorValue(Project.DISCRIMINATOR_VALUE)
class Project(
    name: String,
    description: String,
    archive: Boolean = false,
    photoPath: String? = null,
    color: String? = null
) : AbstractProject(
    name = name,
    description = description,
    archive = archive,
    photoPath = photoPath,
    color = color
) {
    companion object {
        const val DISCRIMINATOR_VALUE = "PROJECT"
    }

    @OneToOne(
        fetch = FetchType.LAZY,
        cascade = [CascadeType.ALL],
        orphanRemoval = true
    )
    @JoinColumn(name = "eventFeedId")
    lateinit var eventFeed: EventFeed
}
