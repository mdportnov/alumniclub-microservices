package ru.mephi.alumniclub.app.controller.routers

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import ru.mephi.alumniclub.app.controller.handler.ReferralHandler
import ru.mephi.alumniclub.app.controller.swagger.ReferralRoutingDoc
import ru.mephi.alumniclub.shared.util.constants.API_VERSION_1
import ru.mephi.alumniclub.shared.util.modernRouter
import ru.mephi.alumniclub.shared.util.response.ResponseHandler

@Configuration
class ReferralRouting(
    private val handler: ReferralHandler,
    private val responseHandler: ResponseHandler
) {
    @Bean
    @ReferralRoutingDoc
    fun referralRouter() = modernRouter(responseHandler) {
        API_VERSION_1.nest {
            "/referral".nest {
                GET("/invite-link", handler::getInviteLink)
                GET("/token", handler::getSelfReferralToken)
                GET(handler::getAllReferrals)
            }
            "/public/referral".nest {
                GET("/invite", handler::handleInvite)
            }
            "/admin/referral".nest {
                GET("/top", handler::getTopNByReferralCount)
                GET("/contains", handler::getAffiliatesByNameContains)
                GET("/{id}", handler::getAllUserReferrals)
            }
        }
    }
}