package ru.mephi.alumniclub.app.database.entity.referral

import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import ru.mephi.alumniclub.app.database.entity.user.User
import ru.mephi.alumniclub.app.model.enumeration.referral.ReferralStatus
import ru.mephi.alumniclub.shared.database.entity.AbstractEntity
import javax.persistence.*

@Entity
@Table(name = "ReferralUser")
class ReferralUser(
    @OnDelete(action = OnDeleteAction.CASCADE)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "affiliateId", nullable = false)
    var affiliate: Affiliate,

    @OnDelete(action = OnDeleteAction.CASCADE)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "referralId", nullable = false)
    var referral: User,

    @Column(name = "status", nullable = false)
    var status: ReferralStatus,
) : AbstractEntity()
