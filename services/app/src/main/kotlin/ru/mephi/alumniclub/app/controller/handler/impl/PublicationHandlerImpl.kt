package ru.mephi.alumniclub.app.controller.handler.impl

import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse
import org.springframework.web.servlet.function.body
import ru.mephi.alumniclub.app.controller.handler.PublicationHandler
import ru.mephi.alumniclub.app.model.dto.ParticipationDto
import ru.mephi.alumniclub.app.model.dto.feed.request.*
import ru.mephi.alumniclub.app.model.dto.meeting.MeetingParticipationRequest
import ru.mephi.alumniclub.app.model.enumeration.feed.DefaultFeed
import ru.mephi.alumniclub.app.service.FeedService
import ru.mephi.alumniclub.app.service.FullPublicationService
import ru.mephi.alumniclub.app.service.PublicationService
import ru.mephi.alumniclub.shared.dto.common.ApiError
import ru.mephi.alumniclub.shared.dto.common.ApiMessage
import ru.mephi.alumniclub.shared.dto.security.ScopePermission
import ru.mephi.alumniclub.shared.util.extension.*
import ru.mephi.alumniclub.shared.util.response.ResponseManager
import java.net.URI
import java.util.*


@Component
class PublicationHandlerImpl(
    private val feedService: FeedService,
    private val publicationService: PublicationService,
    private val publicationHelperService: FullPublicationService
) : ResponseManager(), PublicationHandler {
    override fun listPublicationFeeds(request: ServerRequest): ServerResponse {
        val page = request.getExtendedPageRequest(defaultField = "id")
        val query = request.paramOrElse("query") { "" }.trim()
        val feeds = feedService.listPublicationFeeds(query, page)
        return feeds.toOkBody()
    }

    override fun listPublicationsByDefaultFeedNameForPublic(request: ServerRequest): ServerResponse {
        val feed = request.pathVariableOrThrow<DefaultFeed>("feedName")
        val page = request.getExtendedPageRequest(defaultField = "publicationDate")
        val publications = publicationService.listPublicationsByFeedIdForPublic(feed.id, page)
        return publications.toOkBody()
    }

    override fun listPublicationsByDefaultFeedNameForUser(request: ServerRequest): ServerResponse {
        val userId = request.getPrincipal()
        val feed = request.pathVariableOrThrow<DefaultFeed>("feedName")
        val cursor = request.getCursorRequest()
        val publications = publicationService.listPublicationsByFeedIdForUser(feed.id, userId, cursor)
        return publications.toOkBody()
    }

    override fun listPublicationsByDefaultFeedNameForAdmin(request: ServerRequest): ServerResponse {
        request.assertHasOneOfPermission(ScopePermission.FEEDS_MODIFY)
        val feed = request.pathVariableOrThrow<DefaultFeed>("feedName")
        val page = request.getExtendedPageRequest(defaultField = "publicationDate")
        val query = request.paramOrElse("query") { "" }.trim()
        val publications = publicationService.listPublicationsByFeedIdForAdmin(feed.id, query, page)
        return publications.toOkBody()
    }

    override fun listPublicationsByFeedIdForAdmin(request: ServerRequest): ServerResponse {
        val feedId = request.pathVariableOrThrow<Long>("feedId")
        assertCanViewFeed(request, feedId)
        val page = request.getExtendedPageRequest(defaultField = "publicationDate")
        val query = request.paramOrElse("query") { "" }.trim()
        val publications = publicationService.listPublicationsByFeedIdForAdmin(feedId, query, page)
        return publications.toOkBody()
    }

    override fun listPublicationsByFeedIdForUser(request: ServerRequest): ServerResponse {
        val feedId = request.pathVariableOrThrow<Long>("feedId")
        val userId = request.getPrincipal()
        val cursor = request.getCursorRequest()
        val publications = publicationService.listPublicationsByFeedIdForUser(feedId, userId, cursor)
        return publications.toOkBody()
    }

    override fun listEventsByFeedIdForAdmin(request: ServerRequest): ServerResponse {
        val feedId = request.pathVariableOrThrow<Long>("feedId")
        assertCanViewFeed(request, feedId)
        val query = request.paramOrElse("query") { "" }.trim()
        val tag = request.paramOrElse("tag") { "" }
        val page = request.getExtendedPageRequest(defaultField = "publicationDate")
        val events = publicationService.listEventsByFeedIdForAdmin(feedId, query, tag, page)
        return events.toOkBody()
    }

    override fun listEventsByFeedIdForUser(request: ServerRequest): ServerResponse {
        val feedId = request.pathVariableOrThrow<Long>("feedId")
        val userId = request.getPrincipal()
        val cursor = request.getCursorRequest()
        val events = publicationService.listEventsByFeedIdForUser(feedId, userId, cursor)
        return events.toOkBody()
    }

    override fun listAllPublications(request: ServerRequest): ServerResponse {
        val query = request.paramOrElse("query") { "" }.trim()
        val pageRequest = request.getExtendedPageRequest(defaultField = "publicationDate")
        val publications = publicationService.listAllPublications(query, pageRequest)
        return publications.toOkBody()
    }

    override fun listAllEventsForUser(request: ServerRequest): ServerResponse {
        val userId = request.getPrincipal()
        val cursor = request.getCursorRequest()
        val events = publicationService.listAllEventsForUser(userId, cursor)
        return events.toOkBody()
    }

    override fun listAllEventsForAdmin(request: ServerRequest): ServerResponse {
        request.assertHasOneOfPermission(ScopePermission.FEEDS_MODIFY, ScopePermission.PROJECTS_VIEW)
        val query = request.paramOrElse("query") { "" }.trim()
        val tag = request.paramOrElse("tag") { "" }
        val pageRequest = request.getExtendedPageRequest(defaultField = "publicationDate")
        val events = publicationService.listAllEventsForAdmin(query, tag, pageRequest)
        return events.toOkBody()
    }

    override fun previewEvents(request: ServerRequest): ServerResponse {
        val events = publicationService.previewEvents()
        return events.toOkBody()
    }

    override fun findPublicationByHumanUrl(request: ServerRequest): ServerResponse {
        val url = request.pathVariableOrThrow<String>("url")
        val publication = publicationService.findPublicationByHumanUrl(url)
        return publication.toOkBody()
    }

    override fun findEventByHumanUrl(request: ServerRequest): ServerResponse {
        val url = request.pathVariableOrThrow<String>("url")
        val event = publicationService.findEventByHumanUrl(url)
        return event.toOkBody()
    }

    override fun findPublicationByIdForPublic(request: ServerRequest): ServerResponse {
        val id = request.pathVariableOrThrow<UUID>("id")
        val withRecommendation = request.paramOrElse("recommendation") { false }
        val publication = publicationService.findPublicationByIdForPublic(id, withRecommendation)
        return publication.toOkBody()
    }

    override fun findPublicationByIdForUser(request: ServerRequest): ServerResponse {
        val userId = request.getPrincipal()
        val feedId = request.pathVariableOrThrow<Long>("feedId")
        val publicationId = request.pathVariableOrThrow<UUID>("publicationId")
        val withRecommendation = request.paramOrElse("recommendation") { false }
        val publication = publicationService
            .findPublicationByIdForUser(userId, feedId, publicationId, withRecommendation)
        return publication.toOkBody()
    }

    override fun findPublicationByIdForAdmin(request: ServerRequest): ServerResponse {
        val feedId = request.pathVariableOrThrow<Long>("feedId")
        assertCanViewFeed(request, feedId)
        val publicationId = request.pathVariableOrThrow<UUID>("publicationId")
        val publication = publicationService.findPublicationByIdForAdmin(feedId, publicationId)
        return publication.toOkBody()
    }

    override fun findPublicationByIdInDefaultFeedForUser(request: ServerRequest): ServerResponse {
        val userId = request.getPrincipal()
        val feed = request.pathVariableOrThrow<DefaultFeed>("feedName")
        val publicationId = request.pathVariableOrThrow<UUID>("publicationId")
        val publication = publicationService
            .findPublicationByIdForUser(userId, feed.id, publicationId)
        return publication.toOkBody()
    }

    override fun findPublicationByIdInDefaultFeedForAdmin(request: ServerRequest): ServerResponse {
        request.assertHasOneOfPermission(ScopePermission.FEEDS_MODIFY)
        val feed = request.pathVariableOrThrow<DefaultFeed>("feedName")
        val publicationId = request.pathVariableOrThrow<UUID>("publicationId")
        val publication = publicationService.findPublicationByIdForAdmin(feed.id, publicationId)
        return publication.toOkBody()
    }

    override fun findEventByIdForPublic(request: ServerRequest): ServerResponse {
        val eventId = request.pathVariableOrThrow<UUID>("id")
        return publicationService.findEventByIdForPublic(eventId).toOkBody()
    }

    override fun findEventByIdForUser(request: ServerRequest): ServerResponse {
        val userId = request.getPrincipal()
        val feedId = request.pathVariableOrThrow<Long>("feedId")
        val eventId = request.pathVariableOrThrow<UUID>("publicationId")
        val event = publicationService.findEventByIdForUser(userId, feedId, eventId)
        return event.toOkBody()
    }

    override fun findEventByIdForAdmin(request: ServerRequest): ServerResponse {
        val feedId = request.pathVariableOrThrow<Long>("feedId")
        assertCanViewFeed(request, feedId)
        val eventId = request.pathVariableOrThrow<UUID>("publicationId")
        val event = publicationService.findEventByIdForAdmin(feedId, eventId)
        return event.toOkBody()
    }

    override fun postPublication(request: ServerRequest): ServerResponse {
        val userId = request.getPrincipal()
        val feedId = request.pathVariableOrThrow<Long>("feedId")
        assertCanModifyFeed(request, feedId)
        val publicationRequest = request.body<PublicationRequest>()
        validate(publicationRequest)
        val publication = publicationService.post(userId, feedId, publicationRequest)
        return ApiMessage(
            message = i18n("label.common.created"),
            publication
        ).toCreatedResponse("${request.path()}/${publication.id}")
    }

    override fun postPublicationInDefaultFeed(request: ServerRequest): ServerResponse {
        request.assertHasOneOfPermission(ScopePermission.FEEDS_MODIFY)
        val userId = request.getPrincipal()
        val feed = request.pathVariableOrThrow<DefaultFeed>("feedName")
        val publicationRequest = request.body<PublicationRequest>()
        validate(publicationRequest)
        val publication = publicationService.post(userId, feed.id, publicationRequest)
        return ApiMessage(
            message = i18n("label.common.created"),
            publication
        ).toCreatedResponse("${request.path()}/${publication.id}")
    }

    override fun postEvent(request: ServerRequest): ServerResponse {
        val userId = request.getPrincipal()
        val feedId = request.pathVariableOrThrow<Long>("feedId")
        assertCanModifyFeed(request, feedId)
        val eventRequest = request.body<EventRequest>()
        validate(eventRequest)
        val event = publicationService.post(userId, feedId, eventRequest)
        return ApiMessage(
            message = i18n("label.common.created"),
            event
        ).toCreatedResponse("${request.path()}/${event.id}")
    }

    override fun updatePublication(request: ServerRequest): ServerResponse {
        val feedId = request.pathVariableOrThrow<Long>("feedId")
        assertCanModifyFeed(request, feedId)
        val publicationId = request.pathVariableOrThrow<UUID>("publicationId")
        val publicationRequest = request.body<PublicationUpdateRequest>()
        validate(publicationRequest)
        val publication = publicationService.update(feedId, publicationId, publicationRequest)
        return ApiMessage(message = i18n("label.common.updated"), publication).toOkBody()
    }

    override fun updatePublicationInDefaultFeed(request: ServerRequest): ServerResponse {
        request.assertHasOneOfPermission(ScopePermission.FEEDS_MODIFY)
        val feed = request.pathVariableOrThrow<DefaultFeed>("feedName")
        val publicationId = request.pathVariableOrThrow<UUID>("publicationId")
        val publicationRequest = request.body<PublicationUpdateRequest>()
        validate(publicationRequest)
        val publication = publicationService.update(feed.id, publicationId, publicationRequest)
        return ApiMessage(message = i18n("label.common.updated"), publication).toOkBody()
    }

    override fun updateEvent(request: ServerRequest): ServerResponse {
        val feedId = request.pathVariableOrThrow<Long>("feedId")
        assertCanModifyFeed(request, feedId)
        val eventId = request.pathVariableOrThrow<UUID>("publicationId")
        val eventRequest = request.body<EventUpdateRequest>()
        validate(eventRequest)
        val event = publicationService.update(feedId, eventId, eventRequest)
        return ApiMessage(message = i18n("label.common.updated"), event).toOkBody()
    }

    override fun uploadPublicationPhoto(request: ServerRequest): ServerResponse {
        val feedId = request.pathVariableOrThrow<Long>("feedId")
        assertCanModifyFeed(request, feedId)
        val publicationId = request.pathVariableOrThrow<UUID>("publicationId")
        val file = request.getMultiPartPhoto()
            ?: throw ApiError(HttpStatus.BAD_REQUEST, i18n("exception.media.invalidMultipart"))
        val publication = publicationService.uploadPublicationPhoto(feedId, publicationId, file)
        return ApiMessage(message = i18n("label.common.updated"), publication).toOkBody()
    }

    override fun uploadPublicationPhotoInDefaultFeed(request: ServerRequest): ServerResponse {
        request.assertHasOneOfPermission(ScopePermission.FEEDS_MODIFY)
        val feed = request.pathVariableOrThrow<DefaultFeed>("feedName")
        val publicationId = request.pathVariableOrThrow<UUID>("publicationId")
        val file = request.getMultiPartPhoto()
            ?: throw ApiError(HttpStatus.BAD_REQUEST, i18n("exception.media.invalidMultipart"))
        val publication = publicationService.uploadPublicationPhoto(feed.id, publicationId, file)
        return ApiMessage(message = i18n("label.common.updated"), publication).toOkBody()
    }

    override fun uploadEventPhoto(request: ServerRequest): ServerResponse {
        val feedId = request.pathVariableOrThrow<Long>("feedId")
        assertCanModifyFeed(request, feedId)
        val eventId = request.pathVariableOrThrow<UUID>("publicationId")
        val file = request.getMultiPartPhoto()
            ?: throw ApiError(HttpStatus.BAD_REQUEST, i18n("exception.media.invalidMultipart"))
        val event = publicationService.uploadEventPhoto(feedId, eventId, file)
        return ApiMessage(message = i18n("label.common.updated"), event).toOkBody()
    }

    override fun deletePublication(request: ServerRequest): ServerResponse {
        val feedId = request.pathVariableOrThrow<Long>("feedId")
        assertCanModifyFeed(request, feedId)
        val publicationId = request.pathVariableOrThrow<UUID>("publicationId")
        publicationService.deletePublication(feedId, publicationId)
        return ApiMessage(data = null, message = i18n("label.common.deleted")).toOkBody()
    }

    override fun deletePublicationInDefaultFeed(request: ServerRequest): ServerResponse {
        request.assertHasOneOfPermission(ScopePermission.FEEDS_MODIFY)
        val feed = request.pathVariableOrThrow<DefaultFeed>("feedName")
        val publicationId = request.pathVariableOrThrow<UUID>("publicationId")
        publicationService.deletePublication(feed.id, publicationId)
        return ApiMessage(data = null, message = i18n("label.common.deleted")).toOkBody()
    }

    override fun deleteEvent(request: ServerRequest): ServerResponse {
        val feedId = request.pathVariableOrThrow<Long>("feedId")
        assertCanModifyFeed(request, feedId)
        val eventId = request.pathVariableOrThrow<UUID>("publicationId")
        publicationService.deleteEvent(feedId, eventId)
        return ApiMessage(data = null, message = i18n("label.common.deleted")).toOkBody()
    }

    override fun listLikes(request: ServerRequest): ServerResponse {
        val feedId = request.pathVariableOrThrow<Long>("feedId")
        assertCanViewFeed(request, feedId)
        val publicationId = request.pathVariableOrThrow<UUID>("publicationId")
        val page = request.getExtendedPageRequest()
        val likes = publicationService.listLikes(feedId, publicationId, page)
        return likes.toOkBody()
    }

    override fun listLikesInDefaultFeed(request: ServerRequest): ServerResponse {
        request.assertHasOneOfPermission(ScopePermission.FEEDS_MODIFY)
        val feed = request.pathVariableOrThrow<DefaultFeed>("feedName")
        val publicationId = request.pathVariableOrThrow<UUID>("publicationId")
        val page = request.getExtendedPageRequest()
        val likes = publicationService.listLikes(feed.id, publicationId, page)
        return likes.toOkBody()
    }

    override fun like(request: ServerRequest): ServerResponse {
        val userId = request.getPrincipal()
        val feedId = request.pathVariableOrThrow<Long>("feedId")
        val publicationId = request.pathVariableOrThrow<UUID>("publicationId")
        val likeRequest = request.body<LikeRequest>()
        return ApiMessage(
            data = publicationService.like(userId, feedId, publicationId, likeRequest),
            message = if (likeRequest.like) {
                i18n("label.publication.liked")
            } else i18n("label.publication.disliked")
        ).toOkBody()
    }

    override fun likeInDefaultFeed(request: ServerRequest): ServerResponse {
        val userId = request.getPrincipal()
        val feed = request.pathVariableOrThrow<DefaultFeed>("feedName")
        val publicationId = request.pathVariableOrThrow<UUID>("publicationId")
        val likeRequest = request.body<LikeRequest>()
        return ApiMessage(
            data = publicationService.like(userId, feed.id, publicationId, likeRequest),
            message = if (likeRequest.like) {
                i18n("label.publication.liked")
            } else i18n("label.publication.disliked")
        ).toOkBody()
    }

    override fun listJoins(request: ServerRequest): ServerResponse {
        val feedId = request.pathVariableOrThrow<Long>("feedId")
        assertCanViewFeed(request, feedId)
        val eventId = request.pathVariableOrThrow<UUID>("publicationId")
        val page = request.getExtendedPageRequest()
        val events = publicationService.listJoins(feedId, eventId, page)
        return events.toOkBody()
    }

    override fun participate(request: ServerRequest): ServerResponse {
        val participationDto = request.body<ParticipationDto>()
        val response = publicationService.participate(
            userId = request.getPrincipal(),
            feedId = request.pathVariableOrThrow("feedId"),
            eventId = request.pathVariableOrThrow("publicationId"),
            request = participationDto
        )
        val message = if (participationDto.participation) {
            i18n("label.event.join")
        } else i18n("label.event.leave")
        return ApiMessage(data = response, message = message).toOkBody()
    }

    override fun joinMeeting(request: ServerRequest): ServerResponse {
        val eventId = request.pathVariableOrThrow<UUID>("id")
        val userId = request.getPrincipal()
        val participationRequest = request.body<MeetingParticipationRequest>()
        return ApiMessage(
            data = publicationService.joinMeeting(userId, eventId, participationRequest),
            message = i18n("label.event.joinMeeting")
        ).toOkBody()
    }

    override fun leaveMeeting(request: ServerRequest): ServerResponse {
        val eventId = request.pathVariableOrThrow<UUID>("id")
        val userId = request.getPrincipal()
        publicationService.leaveMeeting(userId, eventId)
        return ApiMessage(data = null, message = i18n("label.event.leaveMeeting")).toOkBody()
    }

    override fun redirectToPublicationById(request: ServerRequest): ServerResponse {
        val id = request.pathVariableOrThrow<UUID>("id")
        val link = publicationHelperService.getRedirectLinkToPublication(id)
        return ServerResponse.permanentRedirect(URI.create(link)).build()
    }

    private fun assertCanViewFeed(request: ServerRequest, feedId: Long) {
        if (canModifyFeed(request, feedId)) return
        request.assertHasOneOfPermission(ScopePermission.PROJECTS_VIEW)
    }

    private fun assertCanModifyFeed(request: ServerRequest, feedId: Long) {
        if (!canModifyFeed(request, feedId))
            throw ApiError(HttpStatus.FORBIDDEN, "exception.forbidden.resource")
    }

    private fun canModifyFeed(request: ServerRequest, feedId: Long): Boolean {
        if (request.hasOneOfPermission(ScopePermission.PROJECTS_CONTROL, ScopePermission.FEEDS_MODIFY))
            return true
        val projects = request.getAuthenticationToken().getPermissions().projects
        return feedService.existsFeedIdInProjects(feedId, projects)
    }
}
