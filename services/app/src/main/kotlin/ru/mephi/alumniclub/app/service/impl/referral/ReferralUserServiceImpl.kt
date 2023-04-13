package ru.mephi.alumniclub.app.service.impl.referral

import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import ru.mephi.alumniclub.app.database.entity.referral.Affiliate
import ru.mephi.alumniclub.app.database.entity.referral.ReferralUser
import ru.mephi.alumniclub.app.database.entity.user.User
import ru.mephi.alumniclub.app.database.repository.referral.AffiliateDao
import ru.mephi.alumniclub.app.database.repository.referral.ReferralUserDao
import ru.mephi.alumniclub.app.model.dto.referral.ReferralResponse
import ru.mephi.alumniclub.app.model.enumeration.referral.ReferralStatus
import ru.mephi.alumniclub.app.model.mapper.referral.ReferralMapper
import ru.mephi.alumniclub.app.service.AffiliateService
import ru.mephi.alumniclub.app.service.ReferralUserService
import ru.mephi.alumniclub.shared.dto.common.ApiError
import ru.mephi.alumniclub.shared.util.response.ResponseManager
import javax.transaction.Transactional

@Service
class ReferralUserServiceImpl(
    private val affiliateService: AffiliateService,
    private val affiliateDao: AffiliateDao,
    private val userReferralDao: ReferralUserDao,
    private val mapper: ReferralMapper,
) : ResponseManager(), ReferralUserService {

    @Transactional
    override fun save(referral: User, token: String): ReferralUser {
        val affiliate = affiliateService.findEntityByToken(token)
        return userReferralDao.save(ReferralUser(affiliate, referral, ReferralStatus.INACTIVE))
    }


    @Transactional
    override fun updateReferralStatus(userId: Long, status: ReferralStatus): ReferralUser {
        val entity = findEntityByReferralId(userId)
        entity.status = status
        userReferralDao.save(entity)
        return entity
    }

    override fun tryUpdateReferralStatus(userId: Long, status: ReferralStatus) {
        if (haveAffiliate(userId))
            updateReferralStatus(userId, status)
    }

    override fun haveAffiliate(userId: Long): Boolean {
        return userReferralDao.findEntityByReferralId(userId).isPresent
    }

    override fun findEntityByReferralId(id: Long): ReferralUser {
        return userReferralDao.findEntityByReferralId(id)
            .orElseThrow { ApiError(HttpStatus.NOT_FOUND, i18n("exception.notFound.affiliate")) }
    }

    override fun findAffiliateByReferralId(userId: Long): Affiliate {
        return affiliateDao.findAffiliateByReferralId(userId)
            .orElseThrow { ApiError(HttpStatus.NOT_FOUND, i18n("exception.notFound.affiliate")) }
    }

    @Transactional
    override fun getAllReferrals(userId: Long): List<ReferralResponse> {
        val affiliate = affiliateService.findEntityByUserId(userId)
        return mapper.asListReferralResponses(affiliate)
    }
}
