package ru.mephi.alumniclub.app.database.entity.user

import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import ru.mephi.alumniclub.shared.database.entity.AbstractEntity
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "RefreshToken")
class RefreshToken(
    @Column(nullable = false)
    var expiresAt: Date,

    @Column(nullable = false, length = 60)
    var hash: String,

    @OnDelete(action = OnDeleteAction.CASCADE)
    @OneToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "fingerprintId")
    var fingerprint: Fingerprint
) : AbstractEntity()
