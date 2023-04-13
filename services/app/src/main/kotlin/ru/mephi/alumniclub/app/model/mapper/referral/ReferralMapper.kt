package ru.mephi.alumniclub.app.model.mapper.referral

import org.springframework.stereotype.Component
import ru.mephi.alumniclub.app.database.entity.referral.Affiliate
import ru.mephi.alumniclub.app.database.entity.referral.ReferralUser
import ru.mephi.alumniclub.app.model.dto.referral.AffiliateResponse
import ru.mephi.alumniclub.app.model.dto.referral.ReferralResponse
import ru.mephi.alumniclub.app.model.dto.referral.ReferralTokenResponse
import ru.mephi.alumniclub.app.model.mapper.user.UserMapper

@Component
class ReferralMapper(
    private val userMapper: UserMapper
) {
    fun asTokenResponse(entity: Affiliate): ReferralTokenResponse {
        return ReferralTokenResponse(entity.user.id, entity.token)
    }

    fun asResponse(entity: Affiliate): AffiliateResponse {
        val user = userMapper.asShortResponse(entity.user)
        return AffiliateResponse(user, entity.token, entity.referrals.size)
    }

    fun asResponse(entity: List<Affiliate>): List<AffiliateResponse> {
        val list: MutableList<AffiliateResponse> = ArrayList(entity.size)
        for (affiliate in entity) {
            list.add(asResponse(affiliate))
        }
        return list
    }

    fun asListReferralResponses(affiliate: Affiliate): List<ReferralResponse> {
        return affiliate.referrals.map(::asReferralResponse)
    }

    fun asReferralResponse(referralUser: ReferralUser): ReferralResponse {
        val user = userMapper.asShortResponse(referralUser.referral)
        return ReferralResponse(user, referralUser.status, referralUser.referral.photoPath)
    }
}