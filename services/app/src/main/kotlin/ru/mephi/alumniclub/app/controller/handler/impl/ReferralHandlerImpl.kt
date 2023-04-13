package ru.mephi.alumniclub.app.controller.handler.impl

import org.springframework.stereotype.Service
import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse
import ru.mephi.alumniclub.app.controller.handler.ReferralHandler
import ru.mephi.alumniclub.app.service.AffiliateService
import ru.mephi.alumniclub.app.service.ReferralUserService
import ru.mephi.alumniclub.shared.dto.security.ScopePermission
import ru.mephi.alumniclub.shared.util.extension.*
import java.net.URI

@Service
class ReferralHandlerImpl(
    private val referralUserService: ReferralUserService,
    private val affiliateService: AffiliateService
) : ReferralHandler {

    override fun getSelfReferralToken(request: ServerRequest): ServerResponse {
        val userId = request.getPrincipal()
        val response = affiliateService.getByUserId(userId)
        return response.toOkBody()
    }

    override fun getAllUserReferrals(request: ServerRequest): ServerResponse {
        request.assertHasOneOfPermission(ScopePermission.USERS)
        val userId = request.pathVariable("id").toLong()
        val response = referralUserService.getAllReferrals(userId)
        return response.toOkBody()
    }

    override fun getAllReferrals(request: ServerRequest): ServerResponse {
        val userId = request.getPrincipal()
        val response = referralUserService.getAllReferrals(userId)
        return response.toOkBody()
    }

    override fun handleInvite(request: ServerRequest): ServerResponse {
        val token = request.paramOrThrow<String>("referralToken")
        val redirectLink = affiliateService.handleInviteLink(token)
        val response = ServerResponse.permanentRedirect(URI.create(redirectLink))
        return response.build()
    }

    override fun getInviteLink(request: ServerRequest): ServerResponse {
        val userId = request.getPrincipal()
        val response = affiliateService.getInviteLink(userId)
        return response.toOkBody()
    }

    override fun getTopNByReferralCount(request: ServerRequest): ServerResponse {
        request.assertHasOneOfPermission(ScopePermission.USERS)
        val count = request.paramOrElse("count") { 10 }
        val response = affiliateService.getNTopByReferralCount(count)
        return response.toOkBody()
    }

    override fun getAffiliatesByNameContains(request: ServerRequest): ServerResponse {
        request.assertHasOneOfPermission(ScopePermission.USERS)
        val string = request.paramOrThrow<String>("string")
        val response = affiliateService.getByNameContains(string)
        return response.toOkBody()
    }
}