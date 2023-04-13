package ru.mephi.alumniclub.app.database.entity.publication

import org.hibernate.annotations.Type
import ru.mephi.alumniclub.app.database.entity.notification.NotificationHolder
import ru.mephi.alumniclub.app.database.entity.user.User
import ru.mephi.alumniclub.shared.util.constants.largeLength
import ru.mephi.alumniclub.shared.util.constants.photoPathLength
import ru.mephi.alumniclub.shared.util.constants.publicationLength
import java.time.LocalDateTime
import java.util.*
import javax.persistence.*

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
abstract class AbstractPublication(
    @Column(nullable = false, length = largeLength)
    override var title: String,

    @Lob
    @Column(nullable = false, length = publicationLength)
    var content: String,

    @Column(nullable = false)
    var viewsCount: Long = 0,

    @Column(length = photoPathLength)
    var photoPath: String? = null
) : NotificationHolder {
    @Id
    @Type(type = "uuid-char")
    override val id: UUID = UUID.randomUUID()

    @Column(name = "createdAt", nullable = false)
    var createdAt: LocalDateTime = LocalDateTime.now()

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "authorId", nullable = true)
    var author: User? = null
}
