package ru.mephi.alumniclub.app.controller.handler.impl

import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse
import org.springframework.web.servlet.function.body
import ru.mephi.alumniclub.app.controller.handler.CommunityHandler
import ru.mephi.alumniclub.app.model.dto.ParticipationDto
import ru.mephi.alumniclub.app.model.dto.community.request.CommunityRequest
import ru.mephi.alumniclub.app.service.CommunityService
import ru.mephi.alumniclub.shared.dto.common.ApiError
import ru.mephi.alumniclub.shared.dto.common.ApiMessage
import ru.mephi.alumniclub.shared.dto.security.ScopePermission
import ru.mephi.alumniclub.shared.util.extension.*
import ru.mephi.alumniclub.shared.util.response.ResponseManager

@Component
class CommunityHandlerImpl(
    private val communityService: CommunityService
) : ResponseManager(), CommunityHandler {

    override fun list(request: ServerRequest): ServerResponse {
        request.assertHasOneOfPermission(ScopePermission.USERS)
        val query = request.paramOrElse("query") { "" }.trim()
        val page = request.getExtendedPageRequest()
        val communities = communityService.list(query, page)
        return communities.toOkBody()
    }

    override fun listSelf(request: ServerRequest): ServerResponse {
        val page = request.getExtendedPageRequest()
        val userId = request.getPrincipal()
        val communities = communityService.listByUser(userId, page, includeHidden = false)
        return communities.toOkBody()
    }

    override fun listNotSelf(request: ServerRequest): ServerResponse {
        val cursor = request.getCursorRequest()
        val userId = request.getPrincipal()
        val communities = communityService.listByUserNot(userId, cursor)
        return communities.toOkBody()
    }

    override fun findById(request: ServerRequest): ServerResponse {
        val id = request.pathVariableOrThrow<Long>("id")
        val includeHidden = request.hasOneOfPermission(ScopePermission.USERS)
        val community = communityService.findById(id, includeHidden)
        return community.toOkBody()
    }

    override fun create(request: ServerRequest): ServerResponse {
        request.assertHasOneOfPermission(ScopePermission.USERS)
        val communityRequest = request.body<CommunityRequest>()
        validate(request)
        val community = communityService.create(communityRequest)
        return ApiMessage(
            message = i18n("label.common.created"),
            community
        ).toCreatedResponse("/api/v1/community/${community.id}")
    }

    override fun update(request: ServerRequest): ServerResponse {
        request.assertHasOneOfPermission(ScopePermission.USERS)
        val id = request.pathVariableOrThrow<Long>("id")
        val communityRequest = request.body<CommunityRequest>()
        validate(request)
        val community = communityService.update(id, communityRequest)
        return ApiMessage(message = i18n("label.common.updated"), community).toOkBody()
    }

    override fun uploadPhoto(request: ServerRequest): ServerResponse {
        request.assertHasOneOfPermission(ScopePermission.USERS)
        val id = request.pathVariableOrThrow<Long>("id")
        val file = request.getMultiPartPhoto()
            ?: throw ApiError(HttpStatus.BAD_REQUEST, i18n("exception.media.invalidMultipart"))
        val community = communityService.uploadPhoto(id, file)
        return ApiMessage(message = i18n("label.common.updated"), community).toOkBody()
    }

    override fun delete(request: ServerRequest): ServerResponse {
        request.assertHasOneOfPermission(ScopePermission.USERS)
        val id = request.pathVariable("id").toLong()
        communityService.delete(id)
        return ApiMessage(data = null, message = i18n("label.common.deleted")).toOkBody()
    }

    override fun listMembersForUser(request: ServerRequest): ServerResponse {
        val id = request.pathVariableOrThrow<Long>("id")
        val query = request.paramOrElse("query") { "" }.trim()
        val page = request.getExtendedPageRequest()
        val members = communityService.listMembersForUser(id, query, page)
        return members.toOkBody()
    }

    override fun listMembersForAdmin(request: ServerRequest): ServerResponse {
        request.assertHasOneOfPermission(ScopePermission.USERS)
        val id = request.pathVariableOrThrow<Long>("id")
        val query = request.paramOrElse("query") { "" }.trim()
        val page = request.getExtendedPageRequest()
        val members = communityService.listMembersForAdmin(id, query, page)
        return members.toOkBody()
    }

    override fun participate(request: ServerRequest): ServerResponse {
        val participationDto = request.body<ParticipationDto>()
        val response = communityService.participate(
            userId = request.getPrincipal(),
            communityId = request.pathVariableOrThrow("id"),
            request = participationDto,
        )
        val message = if (participationDto.participation) {
            i18n("label.community.participation")
        } else i18n("label.community.leave")
        return ApiMessage(data = response, message = message).toOkBody()
    }

    override fun kick(request: ServerRequest): ServerResponse {
        request.assertHasOneOfPermission(ScopePermission.USERS)
        val communityId = request.pathVariableOrThrow<Long>("id")
        val userId = request.paramOrThrow<Long>("userId")
        communityService.participate(userId, communityId, ParticipationDto(false))
        return ApiMessage(data = null, message = i18n("label.community.kick")).toOkBody()
    }
}
