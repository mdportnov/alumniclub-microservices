package ru.mephi.alumniclub.app.service

import ru.mephi.alumniclub.app.database.entity.referral.Affiliate
import ru.mephi.alumniclub.app.model.dto.referral.AffiliateResponse
import ru.mephi.alumniclub.app.model.dto.referral.InviteLinkResponse
import ru.mephi.alumniclub.app.model.dto.referral.ReferralTokenResponse

interface AffiliateService {
    fun findEntityByUserId(userId: Long): Affiliate
    fun findEntityByToken(token: String): Affiliate
    fun getByUserId(userId: Long): ReferralTokenResponse
    fun getInviteLink(userId: Long): InviteLinkResponse
    fun handleInviteLink(token: String): String
    fun tokenIsExist(token: String): Boolean
    fun findNTopByReferralCount(count: Int): List<Affiliate>
    fun getNTopByReferralCount(count: Int): List<AffiliateResponse>
    fun findByNameContains(string: String): List<Affiliate>
    fun getByNameContains(string: String): List<AffiliateResponse>
}