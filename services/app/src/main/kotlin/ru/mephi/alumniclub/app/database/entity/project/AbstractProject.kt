package ru.mephi.alumniclub.app.database.entity.project

import ru.mephi.alumniclub.app.database.entity.community.Community
import ru.mephi.alumniclub.app.database.entity.feed.PublicationFeed
import ru.mephi.alumniclub.app.model.enumeration.ProjectType
import ru.mephi.alumniclub.shared.database.entity.AbstractCreatedAtEntity
import ru.mephi.alumniclub.shared.util.constants.colorLength
import ru.mephi.alumniclub.shared.util.constants.extraLargeLength
import ru.mephi.alumniclub.shared.util.constants.photoPathLength
import ru.mephi.alumniclub.shared.util.constants.smallLength
import javax.persistence.*


@Entity
@Table(name = "Project")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.STRING)
class AbstractProject(
    @Column(nullable = false, length = smallLength)
    var name: String,

    @Column(nullable = false, length = extraLargeLength)
    var description: String,

    @Column(nullable = false)
    var archive: Boolean = false,

    @Column(length = photoPathLength)
    var photoPath: String? = null,

    @Column(length = colorLength)
    var color: String? = null

) : AbstractCreatedAtEntity() {
    fun getType() = ProjectType.valueOf(this.javaClass.getAnnotation(DiscriminatorValue::class.java)!!.value)

    @OneToOne(
        fetch = FetchType.LAZY,
        cascade = [CascadeType.ALL],
        orphanRemoval = true,
        optional = false
    )
    @JoinColumn(name = "publicationFeedId")
    lateinit var publicationFeed: PublicationFeed

    @OneToOne(
        fetch = FetchType.LAZY,
        cascade = [CascadeType.ALL],
        orphanRemoval = true,
        optional = false
    )
    @JoinColumn(name = "communityId")
    lateinit var community: Community
}
