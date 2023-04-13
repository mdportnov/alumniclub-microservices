package ru.mephi.alumniclub.app.controller.swagger

import io.swagger.v3.oas.annotations.Operation
import org.springdoc.core.annotations.RouterOperation
import org.springdoc.core.annotations.RouterOperations
import org.springframework.web.bind.annotation.RequestMethod
import ru.mephi.alumniclub.app.service.AffiliateService
import ru.mephi.alumniclub.app.service.ReferralUserService
import ru.mephi.alumniclub.shared.util.constants.API_VERSION_1


@SwaggerDocumentation
@RouterOperations(
    RouterOperation(
        path = "$API_VERSION_1/referral/token",
        method = arrayOf(RequestMethod.GET),
        beanClass = AffiliateService::class,
        beanMethod = "getByUserId",
        operation = Operation(
            operationId = "referral/token",
            description = """Returns to user self invite link."""
        )
    ),
    RouterOperation(
        path = "$API_VERSION_1/referral/affiliate",
        method = arrayOf(RequestMethod.GET),
        beanClass = ReferralUserService::class,
        beanMethod = "getSelfAffiliate",
        operation = Operation(
            operationId = "referral/affiliate",
            description = """Returns medium information about self affiliate."""
        )
    ),
    RouterOperation(
        path = "$API_VERSION_1/referral",
        method = arrayOf(RequestMethod.GET),
        beanClass = ReferralUserService::class,
        beanMethod = "getAllReferrals",
        operation = Operation(
            operationId = "referral",
            description = """Returns short information about all self referrals."""
        )
    ),
    RouterOperation(
        path = "$API_VERSION_1/referral/invite-link",
        method = arrayOf(RequestMethod.GET),
        beanClass = AffiliateService::class,
        beanMethod = "getInviteLink",
        operation = Operation(
            operationId = "referral/invite-link",
            description = """Returns short information about all self referrals."""
        )
    ),

    RouterOperation(
        path = "$API_VERSION_1/public/referral/invite",
        method = arrayOf(RequestMethod.GET),
        beanClass = AffiliateService::class,
        beanMethod = "handleInviteLink",
        operation = Operation(
            operationId = "public/referral/invite",
            description = """When a person clicks on the invitation link, he gets here.
                It redirects to the registration page, taking into account the referral token"""
        )
    ),


    RouterOperation(
        path = "$API_VERSION_1/admin/referral/top",
        method = arrayOf(RequestMethod.GET),
        beanClass = AffiliateService::class,
        beanMethod = "getNTopByReferralCount",
        operation = Operation(
            operationId = "admin/referral/top",
            description = """Returns top N affiliates by they referrals count."""
        )
    ),
    RouterOperation(
        path = "$API_VERSION_1/admin/referral/contains",
        method = arrayOf(RequestMethod.GET),
        beanClass = AffiliateService::class,
        beanMethod = "getByNameContains",
        operation = Operation(
            operationId = "admin/referral/contains",
            description = """Returns all affiliates info which full name contains smbs."""
        )
    ),
    RouterOperation(
        path = "$API_VERSION_1/admin/referral/{id}",
        method = arrayOf(RequestMethod.GET),
        beanClass = ReferralUserService::class,
        beanMethod = "getAllReferrals",
        operation = Operation(
            operationId = "admin/referral/{id}",
            description = """Returns to admin list of all referrals of entered user."""
        )
    )
)
annotation class ReferralRoutingDoc