package ru.mephi.alumniclub.app.database.entity.referral

import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import ru.mephi.alumniclub.app.database.entity.user.User
import ru.mephi.alumniclub.shared.database.entity.AbstractEntity
import ru.mephi.alumniclub.shared.util.constants.extraSmallLength
import javax.persistence.*

@Entity
@Table(name = "Affiliate")
class Affiliate(
    @OnDelete(action = OnDeleteAction.CASCADE)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id", nullable = false)
    @MapsId
    var user: User,

    @OneToMany(mappedBy = "affiliate", cascade = [CascadeType.ALL])
    var referrals: MutableList<ReferralUser> = mutableListOf(),

    @Column(name = "token", nullable = false, unique = true, length = 36)
    var token: String
) : AbstractEntity()
