package ru.mephi.alumniclub.app.service.impl.referral

import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import ru.mephi.alumniclub.app.config.properties.ServerUrlsProperties
import ru.mephi.alumniclub.app.database.entity.referral.Affiliate
import ru.mephi.alumniclub.app.database.repository.referral.AffiliateDao
import ru.mephi.alumniclub.app.model.dto.referral.AffiliateResponse
import ru.mephi.alumniclub.app.model.dto.referral.InviteLinkResponse
import ru.mephi.alumniclub.app.model.dto.referral.ReferralTokenResponse
import ru.mephi.alumniclub.app.model.exception.common.ResourceNotFoundException
import ru.mephi.alumniclub.app.model.mapper.referral.ReferralMapper
import ru.mephi.alumniclub.app.service.AffiliateService
import ru.mephi.alumniclub.app.service.UserService
import ru.mephi.alumniclub.shared.util.response.ResponseManager
import java.util.*
import javax.transaction.Transactional

@Service
class AffiliateServiceImpl(
    private val dao: AffiliateDao,
    private val userService: UserService,
    private val mapper: ReferralMapper,
    private val serverUrlsProperties: ServerUrlsProperties
) : ResponseManager(), AffiliateService {

    @Transactional
    override fun findEntityByUserId(userId: Long): Affiliate {
        return dao.findById(userId).orElseGet {
            val token = UUID.randomUUID().toString()
            val user = userService.findUserEntityById(userId)
            dao.save(Affiliate(user, mutableListOf(), token))
        }
    }

    override fun findEntityByToken(token: String): Affiliate {
        return dao.findByToken(token)
            .orElseThrow { ResourceNotFoundException(Affiliate::class.java) }
    }

    override fun getByUserId(userId: Long): ReferralTokenResponse {
        return mapper.asTokenResponse(findEntityByUserId(userId))
    }

    override fun getInviteLink(userId: Long): InviteLinkResponse {
        val token = findEntityByUserId(userId).token
        val link = "${serverUrlsProperties.fullBaseUrl}/auth/register?referralToken=$token"
        return InviteLinkResponse(link)
    }

    override fun handleInviteLink(token: String): String {
        return "${serverUrlsProperties.fullBaseUrl}/register?referralToken=$token"
    }

    override fun tokenIsExist(token: String): Boolean {
        return dao.findByToken(token).isPresent
    }

    override fun findNTopByReferralCount(count: Int): List<Affiliate> {
        val pageable = PageRequest.ofSize(count)
        return dao.findTopByReferralCount(pageable).content
    }

    override fun getNTopByReferralCount(count: Int): List<AffiliateResponse> {
        return mapper.asResponse(findNTopByReferralCount(count))
    }

    override fun findByNameContains(string: String): List<Affiliate> {
        return dao.findAffiliatesByContains(string)
    }

    override fun getByNameContains(string: String): List<AffiliateResponse> {
        return mapper.asResponse(findByNameContains(string))
    }
}
