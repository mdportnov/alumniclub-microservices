package ru.mephi.alumniclub.app.database.entity.user

import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import org.springframework.data.jpa.repository.Modifying
import ru.mephi.alumniclub.app.model.enumeration.user.ClientType
import ru.mephi.alumniclub.shared.database.entity.AbstractCreatedAtEntity
import ru.mephi.alumniclub.shared.util.constants.largeLength
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "Fingerprint")
class Fingerprint(

    @Column(nullable = false, length = largeLength)
    val deviceId: String,

    @Column(nullable = true, length = largeLength)
    val name: String?,

    @Column(nullable = true, length = largeLength)
    val version: String?,

    @Enumerated(EnumType.STRING)
    @Column(nullable = true, columnDefinition = "enum( 'WEB', 'ANDROID', 'IOS', 'HUAWEI' )")
    val type: ClientType?

) : AbstractCreatedAtEntity() {

    @Column(nullable = false)
    lateinit var lastUsed: LocalDateTime

    @OnDelete(action = OnDeleteAction.CASCADE)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "userId")
    lateinit var user: User

    @PrePersist
    fun setLastUsed() {
        lastUsed = LocalDateTime.now()
    }

    @Modifying
    @PreUpdate
    fun updateLastUsed() {
        lastUsed = LocalDateTime.now()
    }
}
