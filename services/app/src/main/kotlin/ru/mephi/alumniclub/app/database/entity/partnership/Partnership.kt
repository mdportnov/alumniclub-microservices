package ru.mephi.alumniclub.app.database.entity.partnership

import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import ru.mephi.alumniclub.app.database.entity.user.User
import ru.mephi.alumniclub.shared.database.entity.AbstractCreatedAtEntity
import ru.mephi.alumniclub.shared.util.constants.colorLength
import ru.mephi.alumniclub.shared.util.constants.extraLargeLength
import ru.mephi.alumniclub.shared.util.constants.largeLength
import ru.mephi.alumniclub.shared.util.constants.photoPathLength
import java.time.LocalDateTime
import javax.persistence.*

@Entity
class Partnership(
    @Column(nullable = false, length = largeLength)
    var tag: String,

    @Column(nullable = false, length = colorLength)
    var color: String,

    @Column(nullable = false, length = largeLength)
    var projectName: String,

    @Column(nullable = false, length = extraLargeLength)
    var aboutProject: String,

    @Column(nullable = false, length = extraLargeLength)
    var helpDescription: String,

    @Column(nullable = false)
    var currentUntil: LocalDateTime,

    @Column(nullable = false, length = extraLargeLength)
    var creatorDescription: String,

    @Column(length = photoPathLength)
    var photoPath: String? = null,
) : AbstractCreatedAtEntity() {
    @OnDelete(action = OnDeleteAction.CASCADE)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "authorId", nullable = false)
    lateinit var author: User

    @OnDelete(action = OnDeleteAction.CASCADE)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "creatorId", nullable = false)
    lateinit var creator: User
}