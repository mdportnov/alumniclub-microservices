package ru.mephi.alumniclub.app.service

import org.springframework.web.bind.annotation.PathVariable
import ru.mephi.alumniclub.app.database.entity.referral.Affiliate
import ru.mephi.alumniclub.app.database.entity.referral.ReferralUser
import ru.mephi.alumniclub.app.database.entity.user.User
import ru.mephi.alumniclub.app.model.dto.referral.ReferralResponse
import ru.mephi.alumniclub.app.model.enumeration.referral.ReferralStatus

interface ReferralUserService {
    fun save(referral: User, token: String): ReferralUser
    fun updateReferralStatus(userId: Long, status: ReferralStatus): ReferralUser
    fun tryUpdateReferralStatus(userId: Long, status: ReferralStatus)
    fun haveAffiliate(userId: Long): Boolean

    fun findEntityByReferralId(id: Long): ReferralUser
    fun findAffiliateByReferralId(userId: Long): Affiliate

    fun getAllReferrals(@PathVariable("id") userId: Long): List<ReferralResponse>
}
