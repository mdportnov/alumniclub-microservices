package ru.mephi.alumniclub.app.database.entity.activity


import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import ru.mephi.alumniclub.app.database.entity.user.User
import ru.mephi.alumniclub.shared.database.entity.AbstractEntity
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "UserActivity")
class UserActivity(
    @MapsId
    @OneToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "userId", nullable = false)
    var user: User,
) : AbstractEntity() {
    @Column(name = "time", nullable = false)
    var time: LocalDateTime = LocalDateTime.now()
}