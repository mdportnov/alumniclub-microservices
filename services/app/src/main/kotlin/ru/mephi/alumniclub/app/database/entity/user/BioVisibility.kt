package ru.mephi.alumniclub.app.database.entity.user

import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import java.io.Serializable
import javax.persistence.*

@Entity
@Table(name = "BioVisibility")
class BioVisibility(
    @OnDelete(action = OnDeleteAction.CASCADE)
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "userId")
    @MapsId
    var biography: Biography,

    @Column(nullable = false)
    var country: Boolean = false,

    @Column(nullable = false)
    var city: Boolean = false,

    @Column(nullable = false)
    var jobArea: Boolean = false,

    @Column(nullable = false)
    var company: Boolean = false,

    @Column(nullable = false)
    var job: Boolean = false,

    @Column(nullable = false)
    var experience: Boolean = false,

    @Column(nullable = false)
    var hobbies: Boolean = false,
) : Serializable {
    @Id
    @Column(name = "userId", nullable = false)
    var userId: Long? = null
}
