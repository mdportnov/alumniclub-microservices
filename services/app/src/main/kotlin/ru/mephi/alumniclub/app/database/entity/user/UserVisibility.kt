package ru.mephi.alumniclub.app.database.entity.user

import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import java.io.Serializable
import javax.persistence.*


@Entity
@Table(name = "UserVisibility")
class UserVisibility(
    @OnDelete(action = OnDeleteAction.CASCADE)
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "userId")
    @MapsId
    var user: User,

    @Column(nullable = false)
    var email: Boolean = false,

    @Column(nullable = false)
    var phone: Boolean = false,

    @Column(nullable = false)
    var vk: Boolean = false,

    @Column(nullable = false)
    var tg: Boolean = false,

    @Column(nullable = false)
    var gender: Boolean = false,

    @Column(nullable = false)
    var birthday: Boolean = false,

    @Column(nullable = false)
    var degrees: Boolean = false,

    @Column(nullable = false)
    var createdAt: Boolean = false
) : Serializable {
    @Id
    @Column(name = "userId", nullable = true)
    var userId: Long? = null
}
