package ru.mephi.alumniclub.app.database.entity.partnership

import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import ru.mephi.alumniclub.app.database.entity.user.User
import ru.mephi.alumniclub.shared.database.entity.AbstractCreatedAtEntity
import ru.mephi.alumniclub.shared.util.constants.extraLargeLength
import javax.persistence.*

@Entity
class PartnershipJoin(
    @Column(nullable = false, length = extraLargeLength)
    val contribution: String
) : AbstractCreatedAtEntity() {
    @OnDelete(action = OnDeleteAction.CASCADE)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "userId", nullable = false)
    lateinit var user: User

    @OnDelete(action = OnDeleteAction.CASCADE)
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "partnershipId", nullable = false)
    lateinit var partnership: Partnership
}