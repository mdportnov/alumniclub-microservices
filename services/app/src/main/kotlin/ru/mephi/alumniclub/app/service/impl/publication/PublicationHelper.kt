package ru.mephi.alumniclub.app.service.impl.publication

import org.springframework.context.annotation.Lazy
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import ru.mephi.alumniclub.app.database.entity.feed.EventFeed
import ru.mephi.alumniclub.app.database.entity.feed.PublicationFeed
import ru.mephi.alumniclub.app.database.entity.publication.Event
import ru.mephi.alumniclub.app.database.entity.publication.Publication
import ru.mephi.alumniclub.app.database.repository.feed.EventDao
import ru.mephi.alumniclub.app.database.repository.feed.PublicationDao
import ru.mephi.alumniclub.app.model.dto.broadcast.request.AbstractBroadcastRequest
import ru.mephi.alumniclub.app.model.dto.feed.request.EventRequest
import ru.mephi.alumniclub.app.model.dto.feed.request.PublicationRequest
import ru.mephi.alumniclub.app.model.exception.common.ResourceNotFoundException
import ru.mephi.alumniclub.app.model.mapper.broadcast.BroadcastSenderMapper
import ru.mephi.alumniclub.app.service.BroadcastSenderService
import ru.mephi.alumniclub.app.service.FeedService
import ru.mephi.alumniclub.app.service.ProjectService
import ru.mephi.alumniclub.shared.dto.common.ApiError
import ru.mephi.alumniclub.shared.util.response.ResponseManager
import java.time.LocalDateTime
import java.util.*

@Component
class PublicationHelper(
    private val feedService: FeedService,
    @Lazy private val broadcastSender: BroadcastSenderService,
    @Lazy private val projectService: ProjectService,

    private val broadcastMapper: BroadcastSenderMapper,

    // DAOs
    private val publicationDao: PublicationDao,
    private val eventDao: EventDao,
) : ResponseManager() {
    /**
     * Returns a [Publication] for the given [publicationId] and [feedId].
     *
     * @param feedId The id of the [PublicationFeed].
     * @param publicationId The id of the [Publication].
     * @param ignoreChecks Whether to skip publication date and hidden status check.
     * @return A [Publication] for the given [publicationId] and [feedId].
     * @throws ResourceNotFoundException If the [PublicationFeed] for the given [feedId] does not exist.
     */
    internal fun findPublicationEntity(feedId: Long, publicationId: UUID, ignoreChecks: Boolean = false): Publication {
        if (!feedService.existsPublicationFeedById(feedId))
            throw ResourceNotFoundException(PublicationFeed::class.java, feedId)
        return findPublicationEntity(publicationId, ignoreChecks)
    }

    internal fun findPublicationEntity(id: UUID, ignoreChecks: Boolean): Publication {
        val publication = publicationDao.findById(id)
            .orElseThrow { ResourceNotFoundException(Publication::class.java, id) }
        if (!ignoreChecks && (publication.publicationDate > LocalDateTime.now() || publication.hidden))
            throw ResourceNotFoundException(Publication::class.java)
        return publication
    }

    /**
     * Returns an [Event] for the given [eventId] and [feedId].
     *
     * @param feedId The id of the [EventFeed].
     * @param eventId The id of the [Event].
     * @param ignoreChecks Whether to skip publication date and hidden status check.
     * @return An [Event] for the given [eventId] and [feedId].
     * @throws ResourceNotFoundException If the [EventFeed] for the given [feedId] does not exist.
     */
    internal fun findEventEntity(feedId: Long, eventId: UUID, ignoreChecks: Boolean = false): Event {
        if (!feedService.existsEventFeedById(feedId))
            throw ResourceNotFoundException(EventFeed::class.java, feedId)
        return findEventEntity(eventId, ignoreChecks)
    }

    internal fun findEventEntity(id: UUID, ignoreChecks: Boolean): Event {
        val event = eventDao.findById(id).orElseThrow { ResourceNotFoundException(Event::class.java, id) }
        if (!ignoreChecks && (event.publicationDate > LocalDateTime.now() || event.hidden))
            throw ResourceNotFoundException(Event::class.java, id)
        return event
    }

    /**
     * Validates a publication creation request.
     *
     * @param feedId The ID of the feed for which to create the publication.
     * @param request The [PublicationRequest] containing the details of the publication to create.
     * @throws ResourceNotFoundException if the feed does not exist.
     * @throws ApiError if a publication with the same human URL already exists.
     * @throws ApiError with a forbidden status if the project associated with the feed is archived.
     */
    internal fun validatePublicationCreate(feedId: Long, request: PublicationRequest) {
        if (!feedService.existsPublicationFeedById(feedId))
            throw ResourceNotFoundException(PublicationFeed::class.java, feedId)
        if (publicationDao.existsByHumanUrl(request.humanUrl))
            throw ApiError(HttpStatus.CONFLICT, i18n("exception.alreadyExists.publication"), request.humanUrl)
        if (projectService.findAbstractProjectByAbstractFeedId(feedId)?.archive == true)
            throw ApiError(HttpStatus.FORBIDDEN, "exception.forbidden.projectArchived")
        validate(request)
    }

    /**
     * Validates an event creation request.
     *
     * @param feedId The ID of the feed for which to create the event.
     * @param request The [PublicationRequest] containing the details of the event to create.
     * @throws ResourceNotFoundException if the feed does not exist.
     * @throws ApiError if an event with the same human URL already exists.
     * @throws ApiError with a forbidden status if the project associated with the feed is archived.
     */
    internal fun validateEventCreate(feedId: Long, request: EventRequest) {
        if (!feedService.existsEventFeedById(feedId))
            throw ResourceNotFoundException(EventFeed::class.java, feedId)
        if (eventDao.existsByHumanUrl(request.humanUrl))
            throw ApiError(HttpStatus.CONFLICT, i18n("exception.alreadyExists.event"), request.humanUrl)
        if (projectService.findAbstractProjectByAbstractFeedId(feedId)?.archive == true)
            throw ApiError(HttpStatus.FORBIDDEN, "exception.forbidden.projectArchived")
        validate(request)
    }

    /**
     * Sends a broadcast for a [publication] if the publication date is before or equal to current time + 1 minute.
     *
     * @param userId The ID of the user who is creating the broadcast.
     * @param request The [AbstractBroadcastRequest] that holds the information for creating the broadcast.
     * @param publication The [Publication] for which to create the broadcast.
     */
    internal fun sendBroadcastForPublication(
        userId: Long,
        request: AbstractBroadcastRequest,
        publication: Publication
    ) {
        if (publication.publicationDate <= LocalDateTime.now().plusMinutes(1)) {
            broadcastSender.createBroadcast(userId, broadcastMapper.asBroadcastByPublication(publication, request))
        }
    }

    /**
     * Sends a broadcast for a [event] if the publication date is before or equal to current time + 1 minute.
     *
     * @param userId The ID of the user who is creating the broadcast.
     * @param request The [AbstractBroadcastRequest] that holds the information for creating the broadcast.
     * @param event The [Event] for which to create the broadcast.
     */
    internal fun sendBroadcastForEvent(userId: Long, request: AbstractBroadcastRequest, event: Event) {
        if (event.publicationDate <= LocalDateTime.now().plusMinutes(1)) {
            broadcastSender.createBroadcast(userId, broadcastMapper.asBroadcastByPublication(event, request))
        }
    }
}