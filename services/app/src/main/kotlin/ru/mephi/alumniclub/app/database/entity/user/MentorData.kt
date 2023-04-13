package ru.mephi.alumniclub.app.database.entity.user

import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import ru.mephi.alumniclub.shared.util.constants.extraLargeLength
import ru.mephi.alumniclub.shared.util.constants.mediumLength
import ru.mephi.alumniclub.shared.util.constants.userDataLength
import java.io.Serializable
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "MentorData")
class MentorData(
    @Column(name = "company", nullable = false, length = userDataLength)
    var company: String,

    @Column(name = "position", nullable = false, length = userDataLength)
    var position: String,

    @Column(name = "expertArea", nullable = false, length = userDataLength)
    var expertArea: String,

    @Column(name = "whyAreYouMentor", nullable = false, length = userDataLength)
    var whyAreYouMentor: String,

    @Column(name = "graduation", nullable = false, length = userDataLength)
    var graduation: String,

    @Column(name = "formatsOfInteractions", nullable = false, length = userDataLength)
    var formatsOfInteractions: String,

    @Column(name = "tags", nullable = false, length = userDataLength)
    var tags: String,

    @Column(name = "interviewLink", nullable = true, length = mediumLength)
    var interviewLink: String?,

    @Column(name = "available", nullable = false)
    var available: Boolean = true,

    @OnDelete(action = OnDeleteAction.CASCADE)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", nullable = false)
    @MapsId
    var user: User,
) : Serializable {

    @Id
    @Column(name = "userId", nullable = false)
    var userId: Long? = null

    @CreationTimestamp
    @Column(nullable = false)
    var createdAt: LocalDateTime? = null
}
