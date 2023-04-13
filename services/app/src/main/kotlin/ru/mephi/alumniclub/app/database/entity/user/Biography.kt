package ru.mephi.alumniclub.app.database.entity.user

import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import ru.mephi.alumniclub.shared.util.constants.extraLargeLength
import ru.mephi.alumniclub.shared.util.constants.userDataLength
import java.io.Serializable
import javax.persistence.*

@Entity
@Table(name = "Biography")
class Biography(
    @OnDelete(action = OnDeleteAction.CASCADE)
    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "userId")
    var user: User,

    @Column(length = userDataLength)
    var country: String? = null,

    @Column(length = userDataLength)
    var city: String? = null,

    @Column(length = userDataLength)
    var jobArea: String? = null,

    @Column(length = userDataLength)
    var company: String? = null,

    @Column(length = userDataLength)
    var job: String? = null,

    @Column(length = userDataLength)
    var experience: String? = null,

    @Column(length = userDataLength)
    var hobbies: String? = null,
) : Serializable {
    @Id
    @Column(name = "userId", nullable = false)
    var userId: Long? = null
}
