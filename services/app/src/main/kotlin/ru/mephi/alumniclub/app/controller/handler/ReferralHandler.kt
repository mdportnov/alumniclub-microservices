package ru.mephi.alumniclub.app.controller.handler

import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse

interface ReferralHandler {
    fun getSelfReferralToken(request: ServerRequest): ServerResponse
    fun getAllUserReferrals(request: ServerRequest): ServerResponse
    fun getAllReferrals(request: ServerRequest): ServerResponse
    fun handleInvite(request: ServerRequest): ServerResponse
    fun getInviteLink(request: ServerRequest): ServerResponse
    fun getTopNByReferralCount(request: ServerRequest): ServerResponse
    fun getAffiliatesByNameContains(request: ServerRequest): ServerResponse
}