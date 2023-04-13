package ru.mephi.alumniclub.app.controller.handler

import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse

interface PublicationHandler {
    fun listPublicationFeeds(request: ServerRequest): ServerResponse
    fun listPublicationsByDefaultFeedNameForPublic(request: ServerRequest): ServerResponse
    fun listPublicationsByDefaultFeedNameForUser(request: ServerRequest): ServerResponse
    fun listPublicationsByDefaultFeedNameForAdmin(request: ServerRequest): ServerResponse
    fun listPublicationsByFeedIdForAdmin(request: ServerRequest): ServerResponse
    fun listPublicationsByFeedIdForUser(request: ServerRequest): ServerResponse
    fun listEventsByFeedIdForAdmin(request: ServerRequest): ServerResponse
    fun listEventsByFeedIdForUser(request: ServerRequest): ServerResponse
    fun listAllPublications(request: ServerRequest): ServerResponse
    fun listAllEventsForUser(request: ServerRequest): ServerResponse
    fun listAllEventsForAdmin(request: ServerRequest): ServerResponse
    fun previewEvents(request: ServerRequest): ServerResponse
    fun findPublicationByHumanUrl(request: ServerRequest): ServerResponse
    fun findEventByHumanUrl(request: ServerRequest): ServerResponse
    fun findPublicationByIdForPublic(request: ServerRequest): ServerResponse
    fun findPublicationByIdForUser(request: ServerRequest): ServerResponse
    fun findPublicationByIdForAdmin(request: ServerRequest): ServerResponse
    fun findPublicationByIdInDefaultFeedForUser(request: ServerRequest): ServerResponse
    fun findPublicationByIdInDefaultFeedForAdmin(request: ServerRequest): ServerResponse
    fun findEventByIdForPublic(request: ServerRequest): ServerResponse
    fun findEventByIdForUser(request: ServerRequest): ServerResponse
    fun findEventByIdForAdmin(request: ServerRequest): ServerResponse
    fun postPublication(request: ServerRequest): ServerResponse
    fun postPublicationInDefaultFeed(request: ServerRequest): ServerResponse
    fun postEvent(request: ServerRequest): ServerResponse
    fun updatePublication(request: ServerRequest): ServerResponse
    fun updatePublicationInDefaultFeed(request: ServerRequest): ServerResponse
    fun updateEvent(request: ServerRequest): ServerResponse
    fun uploadPublicationPhoto(request: ServerRequest): ServerResponse
    fun uploadPublicationPhotoInDefaultFeed(request: ServerRequest): ServerResponse
    fun uploadEventPhoto(request: ServerRequest): ServerResponse
    fun deletePublication(request: ServerRequest): ServerResponse
    fun deletePublicationInDefaultFeed(request: ServerRequest): ServerResponse
    fun deleteEvent(request: ServerRequest): ServerResponse
    fun listLikes(request: ServerRequest): ServerResponse
    fun listLikesInDefaultFeed(request: ServerRequest): ServerResponse
    fun like(request: ServerRequest): ServerResponse
    fun likeInDefaultFeed(request: ServerRequest): ServerResponse
    fun listJoins(request: ServerRequest): ServerResponse
    fun participate(request: ServerRequest): ServerResponse
    fun joinMeeting(request: ServerRequest): ServerResponse
    fun leaveMeeting(request: ServerRequest): ServerResponse
    fun redirectToPublicationById(request: ServerRequest): ServerResponse
}
