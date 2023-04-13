package ru.mephi.alumniclub.app.service.impl.publication

import org.springframework.cache.annotation.CacheEvict
import org.springframework.context.annotation.Lazy
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.repository.Modifying
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import ru.mephi.alumniclub.app.database.entity.feed.EventFeed
import ru.mephi.alumniclub.app.database.entity.feed.PublicationFeed
import ru.mephi.alumniclub.app.database.entity.publication.AbstractPublication
import ru.mephi.alumniclub.app.database.entity.publication.Event
import ru.mephi.alumniclub.app.database.entity.publication.Publication
import ru.mephi.alumniclub.app.database.repository.feed.EventDao
import ru.mephi.alumniclub.app.database.repository.feed.JoinDao
import ru.mephi.alumniclub.app.database.repository.feed.LikeDao
import ru.mephi.alumniclub.app.database.repository.feed.PublicationDao
import ru.mephi.alumniclub.app.model.dto.CursorResponse
import ru.mephi.alumniclub.app.model.dto.PageResponse
import ru.mephi.alumniclub.app.model.dto.ParticipationDto
import ru.mephi.alumniclub.app.model.dto.feed.request.*
import ru.mephi.alumniclub.app.model.dto.feed.response.EventParticipationResponse
import ru.mephi.alumniclub.app.model.dto.feed.response.JoinResponse
import ru.mephi.alumniclub.app.model.dto.feed.response.LikeResponse
import ru.mephi.alumniclub.app.model.dto.feed.response.LikesCountResponse
import ru.mephi.alumniclub.app.model.dto.feed.response.admin.EventResponseForAdmin
import ru.mephi.alumniclub.app.model.dto.feed.response.admin.EventShortResponseForAdmin
import ru.mephi.alumniclub.app.model.dto.feed.response.admin.PublicationResponseForAdmin
import ru.mephi.alumniclub.app.model.dto.feed.response.admin.PublicationShortResponseForAdmin
import ru.mephi.alumniclub.app.model.dto.feed.response.user.*
import ru.mephi.alumniclub.app.model.dto.meeting.MeetingParticipationRequest
import ru.mephi.alumniclub.app.model.exception.common.ResourceNotFoundException
import ru.mephi.alumniclub.app.model.mapper.feed.EventMapper
import ru.mephi.alumniclub.app.model.mapper.feed.JoinMapper
import ru.mephi.alumniclub.app.model.mapper.feed.LikeMapper
import ru.mephi.alumniclub.app.model.mapper.feed.PublicationMapper
import ru.mephi.alumniclub.app.service.*
import ru.mephi.alumniclub.shared.dto.ExtendedPageRequest
import ru.mephi.alumniclub.shared.dto.common.ApiError
import ru.mephi.alumniclub.shared.dto.photo.ContentPhotoRequest
import ru.mephi.alumniclub.shared.model.enums.StoreDir
import ru.mephi.alumniclub.shared.util.Cursor
import ru.mephi.alumniclub.shared.util.constants.LIKES_COUNT_CACHE
import ru.mephi.alumniclub.shared.util.response.ResponseManager
import java.time.LocalDateTime
import java.util.*
import javax.servlet.http.Part
import javax.transaction.Transactional

@Service
@Transactional
class PublicationServiceImpl(
    // Storage Manager
    private val storageManager: StorageManager,

    // DAOs
    private val publicationDao: PublicationDao,
    private val eventDao: EventDao,
    private val likeDao: LikeDao,
    private val joinDao: JoinDao,

    // Mappers
    private val publicationMapper: PublicationMapper,
    private val eventMapper: EventMapper,
    private val likeMapper: LikeMapper,
    private val joinMapper: JoinMapper,

    // Services
    private val userService: UserService,
    private val feedService: FeedService,
    private val likeService: LikeService,
    private val contentPhotoService: ContentPhotoService,

    // Lazy Services
    @Lazy private val projectService: ProjectService,
    @Lazy private val joinService: JoinService,

    private val publicationHelper: PublicationHelper

) : ResponseManager(), PublicationService {
    /**
     * Returns a [Publication] entity identified by [id].
     *
     * @param id The UUID of the [Publication] entity to be retrieved.
     * @param ignoreChecks Whether to skip publication date and hidden status check.
     * @return The [Publication] entity with the specified [id].
     * @throws ResourceNotFoundException If the [Publication] entity with the specified [id]
     * does not exist, or the publication date is after the current time or the [Publication] is hidden
     * and [ignoreChecks] is set to false.
     */
    override fun findPublicationEntity(id: UUID, ignoreChecks: Boolean): Publication {
        return publicationHelper.findPublicationEntity(id, ignoreChecks)
    }

    /**
     * Retrieves an [Event] entity from the database.
     *
     * @param id The unique identifier of the [Event] entity to retrieve.
     * @param ignoreChecks Whether to skip publication date and hidden status check.
     * @return The [Event] entity with the specified [id].
     */
    override fun findEventEntity(id: UUID, ignoreChecks: Boolean): Event {
        return publicationHelper.findEventEntity(id, ignoreChecks)
    }


    /**
     * Returns a [PublicationResponseForUser] for the publication with the specified [id].
     *
     * @param id The UUID of the publication to retrieve.
     * @param withRecommendation A flag indicating whether to include the recommendation for the publication.
     * @return A [PublicationResponseForUser] for the publication with the specified [id].
     */
    @Modifying
    override fun findPublicationByIdForPublic(id: UUID, withRecommendation: Boolean): PublicationResponseForUser {
        val publication = findPublicationEntity(id).apply { viewsCount++ }
        return publicationMapper.asResponseForPublic(publication)
    }

    /**
     * Returns a [PublicationResponseForUser] for the specified publication ID.
     *
     * @param userId The ID of the user for whom to retrieve the publication.
     * @param feedId The ID of the feed to which the publication belongs.
     * @param publicationId The ID of the publication to retrieve.
     * @param withRecommendation Whether to include recommendations with the publication.
     * @return A [PublicationResponseForUser] for the specified publication ID.
     */
    @Modifying
    override fun findPublicationByIdForUser(
        userId: Long, feedId: Long, publicationId: UUID, withRecommendation: Boolean
    ): PublicationResponseForUser {
        val publication = publicationHelper.findPublicationEntity(feedId, publicationId).apply { viewsCount++ }
        return publicationMapper.asResponseForUser(userId, publication)
    }

    /**
     * Returns a [PublicationResponseForUser] for the specified human-readable [url].
     *
     * @param url The human-readable URL of the desired publication.
     * @return A [PublicationResponseForUser] for the specified human-readable [url].
     * @throws ResourceNotFoundException if the publication with the specified [url] does not exist.
     */
    @Modifying
    override fun findPublicationByHumanUrl(url: String): PublicationResponseForUser {
        val publication = publicationDao.findByHumanUrlAndPublicationDateIsBefore(url)
            .orElseThrow { ResourceNotFoundException(Publication::class.java, url) }
        if (publication.hidden) throw ResourceNotFoundException(Publication::class.java)
        publication.viewsCount++
        return publicationMapper.asResponseForPublic(publication)
    }

    /**
     * Finds the [Event] with the given [url] and returns it as an [EventResponseForUser].
     *
     * @param url The human-readable URL of the event to retrieve.
     * @return An [EventResponseForUser] representation of the [Event] with the given [url].
     * @throws ResourceNotFoundException if the event does not exist or is hidden.
     */
    @Modifying
    override fun findEventByHumanUrl(url: String): EventResponseForUser {
        val event = eventDao.findByHumanUrlAndPublicationDateIsBefore(url)
            .orElseThrow { ResourceNotFoundException(Event::class.java, url) }
        if (event.hidden) throw ResourceNotFoundException(Publication::class.java)
        event.viewsCount++
        return eventMapper.asResponseForPublic(event)
    }

    /**
     * Returns a [PublicationResponseForAdmin] for the provided [publicationId] and [feedId].
     *
     * @param feedId The ID of the feed for which to retrieve the publication.
     * @param publicationId The ID of the publication to retrieve.
     * @return A [PublicationResponseForAdmin] for the specified publication.
     * @throws ResourceNotFoundException if the publication does not exist.
     */
    override fun findPublicationByIdForAdmin(
        feedId: Long, publicationId: UUID
    ): PublicationResponseForAdmin {
        val publication = publicationHelper.findPublicationEntity(feedId, publicationId, true)
        return publicationMapper.asResponseForAdmin(publication)
    }

    /**
     * Returns a [EventResponseForUser] for a given [eventId].
     *
     * @param userId The ID of the user for whom to retrieve the event.
     * @param feedId The ID of the feed for which to retrieve the event.
     * @param eventId The ID of the event to retrieve.
     * @return A [EventResponseForUser] that match the provided [feedId] and [eventId].
     * @throws ResourceNotFoundException if the event is hidden or the event does not exist.
     */
    @Modifying
    override fun findEventByIdForUser(
        userId: Long, feedId: Long, eventId: UUID
    ): EventResponseForUser {
        val event = publicationHelper.findEventEntity(feedId, eventId)
        if (event.hidden) throw ResourceNotFoundException(Publication::class.java)
        event.viewsCount++
        return eventMapper.asResponseForUser(userId, event)
    }

    /**
     * Returns the [EventResponseForUser] for a given [eventId].
     *
     * @param eventId The ID of the event to retrieve.
     * @return The [EventResponseForUser] for the specified [eventId].
     * @throws ResourceNotFoundException if the event with the specified [eventId] does not exist or is hidden.
     */
    @Modifying
    override fun findEventByIdForPublic(eventId: UUID): EventResponseForUser {
        val event = eventDao.findById(eventId).orElseThrow { ResourceNotFoundException(Event::class.java, eventId) }
        if (event.hidden) throw ResourceNotFoundException(Event::class.java)
        eventDao.incrementViewsCount(event.id)
        return eventMapper.asResponseForPublic(event)
    }

    /**
     * Returns an [EventResponseForAdmin] of the event with the provided [eventId].
     *
     * @param feedId The ID of the feed in which the event resides.
     * @param eventId The ID of the event to retrieve.
     * @return An [EventResponseForAdmin] of the event with the provided [eventId].
     * @throws ResourceNotFoundException if the event does not exist.
     */
    override fun findEventByIdForAdmin(feedId: Long, eventId: UUID): EventResponseForAdmin {
        val event = publicationHelper.findEventEntity(feedId, eventId, ignoreChecks = true)
        return eventMapper.asResponseForAdmin(event)
    }

    /**
     * Returns a paginated list of [PublicationShortResponseForUser] filtered by
     * [userId], [feedId], [query], and [pageRequest].
     *
     * @param userId The ID of the user used to filter the list of publications.
     * @param feedId The ID of the feed used to filter the list of publications.
     * @param query The query string used to filter the list of publications by title.
     * @param pageRequest The [ExtendedPageRequest] used to paginate the list of publications.
     * @return A paginated list of [PublicationShortResponseForUser] that match the provided
     * [userId], [feedId], [query], and [pageRequest].
     * @throws ResourceNotFoundException when the publication feed with the specified [feedId] does not exist.
     * @throws ApiError when the specified publication feed is archived.
     */
    override fun listPublicationsByFeedIdForUser(
        userId: Long, feedId: Long, query: String, pageRequest: ExtendedPageRequest
    ): PageResponse<PublicationShortResponseForUser> {
        if (!feedService.existsPublicationFeedById(feedId))
            throw ResourceNotFoundException(PublicationFeed::class.java, feedId)
        if (projectService.findAbstractProjectByAbstractFeedId(feedId)?.archive == true)
            throw ApiError(HttpStatus.FORBIDDEN, i18n("exception.forbidden.resource"))
        val publications =
            publicationDao.findByFeedIdAndTitleStartsWithAndHiddenFalse(feedId, query, pageRequest.pageable)
        return publicationMapper.asPageResponseForUser(userId, publications)
    }

    /**
     * Returns a paginated list of [EventShortResponseForUser] from the database.
     *
     * @param userId The identifier of the user for whom to retrieve events.
     * @param feedId The identifier of the event feed used to filter the list of events.
     * @param query The query string used to filter the list of events by title.
     * @param tag The tag used to filter the list of events.
     * @param pageRequest The [ExtendedPageRequest] used to paginate the list of events.
     * @return A paginated list of [EventShortResponseForUser].
     * @throws ResourceNotFoundException if the event feed with the given [feedId] does not exist.
     * @throws ApiError if the associated project is archived.
     */
    override fun listEventsByFeedIdForUser(
        userId: Long, feedId: Long, query: String, tag: String, pageRequest: ExtendedPageRequest
    ): PageResponse<EventShortResponseForUser> {
        if (!feedService.existsEventFeedById(feedId)) throw ResourceNotFoundException(EventFeed::class.java, feedId)
        if (projectService.findAbstractProjectByAbstractFeedId(feedId)?.archive == true)
            throw ApiError(HttpStatus.FORBIDDEN, i18n("exception.forbidden.resource"))
        val events = eventDao.findByFeedIdAndTitleStartsWithAndTagStartsWithAndHiddenFalse(
            feedId, query, tag, pageRequest.pageable
        )
        return eventMapper.asPageResponseForUser(userId, events)
    }

    /**
     * Returns a paginated list of [PublicationShortResponseForUser] filtered by chronology.
     *
     * @param feedId The ID of the feed for which to retrieve the publications.
     * @param userId The ID of the user for whom to retrieve the publications.
     * @param cursor The [Cursor] used to filter and paginate the list of publications.
     * @return A paginated list of [PublicationShortResponseForUser] that match the provided [feedId] and [userId].
     * @throws ResourceNotFoundException if the feed does not exist.
     * @throws ApiError if the project associated with the feed is archived.
     */
    override fun listPublicationsByFeedIdForUser(
        feedId: Long, userId: Long, cursor: Cursor
    ): CursorResponse<PublicationShortResponseForUser> {
        if (!feedService.existsPublicationFeedById(feedId)) throw ResourceNotFoundException(
            PublicationFeed::class.java, feedId
        )
        if (projectService.findAbstractProjectByAbstractFeedId(feedId)?.archive == true)
            throw ApiError(HttpStatus.FORBIDDEN, i18n("exception.forbidden.resource"))
        val publications = when (cursor.chronology) {
            Cursor.Chronology.BEFORE -> publicationDao.findByFeedIdAndCreatedAtBeforeAndPublicationDateIsBeforeAndHiddenFalse(
                feedId, cursor.from, cursor.page
            )

            Cursor.Chronology.AFTER -> publicationDao.findByFeedIdAndCreatedAtAfterAndPublicationDateIsBeforeAndHiddenFalse(
                feedId, cursor.from, cursor.page
            )
        }
        return publicationMapper.asCursorResponseForUser(userId, publications)
    }

    /**
     * Returns a paginated list of [PublicationShortResponseForAdmin] filtered by the given [query].
     *
     * @param feedId The ID of the feed for which to retrieve the publications.
     * @param query The search query used to filter the list of publications.
     * @param pageRequest The [ExtendedPageRequest] used to paginate the list of publications.
     * @return A paginated list of [PublicationShortResponseForAdmin] that match the provided [feedId] and [query].
     * @throws ResourceNotFoundException if the feed does not exist.
     */
    override fun listPublicationsByFeedIdForAdmin(
        feedId: Long, query: String, pageRequest: ExtendedPageRequest
    ): PageResponse<PublicationShortResponseForAdmin> {
        if (!feedService.existsPublicationFeedById(feedId))
            throw ResourceNotFoundException(PublicationFeed::class.java, feedId)
        val publications = publicationDao.findByFeedIdAndTitleStartsWith(feedId, query, pageRequest.pageable)
        return publicationMapper.asPageResponseForAdmin(publications)
    }

    /**
     * Returns a paginated list of [PublicationShortResponseForAdmin].
     *
     * @param feedId The ID of the feed for which to retrieve the publications.
     * @param cursor The [Cursor] used to paginate the list of publications.
     * @return A paginated list of [PublicationShortResponseForAdmin] that match the provided [feedId].
     * @throws ResourceNotFoundException if the feed does not exist.
     */
    override fun listEventsByFeedIdForUser(
        feedId: Long, userId: Long, cursor: Cursor
    ): CursorResponse<EventShortResponseForUser> {
        if (!feedService.existsEventFeedById(feedId)) throw ResourceNotFoundException(EventFeed::class.java, feedId)
        if (projectService.findAbstractProjectByAbstractFeedId(feedId)?.archive == true)
            throw ApiError(HttpStatus.FORBIDDEN, i18n("exception.forbidden.resource"))
        val events = when (cursor.chronology) {
            Cursor.Chronology.BEFORE -> eventDao.findByFeedIdAndCreatedAtBeforeAndPublicationDateIsBeforeAndHiddenFalse(
                feedId, cursor.from, cursor.page
            )

            Cursor.Chronology.AFTER -> eventDao.findByFeedIdAndCreatedAtAfterAndPublicationDateIsBeforeAndHiddenFalse(
                feedId, cursor.from, cursor.page
            )
        }
        return eventMapper.asCursorResponseForUser(userId, events)
    }

    /**
     * Returns a paginated list of [EventShortResponseForAdmin] filtered by [feedId], [query] and [tag].
     *
     * @param feedId The ID of the feed for which to retrieve the events.
     * @param query The search query used to filter the events.
     * @param tag The tag used to filter the events.
     * @param pageRequest The [ExtendedPageRequest] used to paginate the list of events.
     * @return A paginated list of [EventShortResponseForAdmin] that match the provided [feedId], [query] and [tag].
     * @throws ResourceNotFoundException if the feed does not exist.
     */
    override fun listEventsByFeedIdForAdmin(
        feedId: Long, query: String, tag: String, pageRequest: ExtendedPageRequest
    ): PageResponse<EventShortResponseForAdmin> {
        if (!feedService.existsEventFeedById(feedId)) throw ResourceNotFoundException(EventFeed::class.java, feedId)
        val events = eventDao.findByFeedIdAndTitleStartsWithAndTagStartsWith(feedId, query, tag, pageRequest.pageable)
        return eventMapper.asPageResponseForAdmin(events)
    }

    /**
     * Returns a paginated list of [PublicationShortResponseForUser] for public access.
     *
     * @param feedId The ID of the feed for which to retrieve the publications.
     * @param pageRequest The [ExtendedPageRequest] used to paginate the list of publications.
     * @return A paginated list of [PublicationShortResponseForUser] that match the provided [feedId].
     * @throws ResourceNotFoundException if the feed does not exist.
     * @throws ApiError if the project associated with the feed is archived.
     */
    override fun listPublicationsByFeedIdForPublic(
        feedId: Long, pageRequest: ExtendedPageRequest
    ): PageResponse<PublicationShortResponseForUser> {
        if (!feedService.existsPublicationFeedById(feedId))
            throw ResourceNotFoundException(PublicationFeed::class.java, feedId)
        if (projectService.findAbstractProjectByAbstractFeedId(feedId)?.archive == true)
            throw ApiError(HttpStatus.FORBIDDEN, i18n("exception.forbidden.resource"))
        val publications =
            publicationDao.findByFeedIdAndPublicationDateIsBeforeAndHiddenFalse(feedId, pageRequest.pageable)
        return publicationMapper.asPageResponseForPublic(publications)
    }

    /**
     * Returns a paginated list of [PublicationShortResponseForUser] filtered by [query].
     *
     * @param query The string used to filter publications by title.
     * @param pageRequest The [ExtendedPageRequest] used to paginate the list of publications.
     * @return A paginated list of [PublicationShortResponseForUser] filtered by title.
     */
    override fun listAllPublications(
        query: String, pageRequest: ExtendedPageRequest,
    ): PageResponse<PublicationShortResponseForUser> {
        val publications =
            publicationDao.findByTitleStartsWithAndPublicationDateIsBeforeAndHiddenFalse(query, pageRequest.pageable)
        return publicationMapper.asPageResponseForPublic(publications)
    }

    /**
     * Returns a paginated list of [EventShortResponseForUser] filtered by chronology.
     *
     * @param userId The ID of the user for whom to retrieve the events.
     * @param cursor The [Cursor] used to filter and paginate the list of events.
     * @return A paginated list of [EventShortResponseForUser] that match the provided [userId].
     */
    override fun listAllEventsForUser(userId: Long, cursor: Cursor): CursorResponse<EventShortResponseForUser> {
        val events = when (cursor.chronology) {
            Cursor.Chronology.BEFORE -> eventDao.findByCreatedAtBeforeAndPublicationDateIsBeforeAndHiddenFalse(
                cursor.from, cursor.page
            )

            Cursor.Chronology.AFTER -> eventDao.findByCreatedAtAfterAndPublicationDateIsBeforeAndHiddenFalse(
                cursor.from, cursor.page
            )
        }
        return eventMapper.asCursorResponseForUser(userId, events)
    }

    /**
     * Returns a paginated list of [EventShortResponseForAdmin] filtered by title and tag.
     *
     * @param query The title query used to filter the list of events.
     * @param tag The tag query used to filter the list of events.
     * @param pageRequest The [ExtendedPageRequest] used to paginate the list of events.
     * @return A paginated list of [EventShortResponseForAdmin] that match the provided title and tag queries.
     */
    override fun listAllEventsForAdmin(
        query: String, tag: String, pageRequest: ExtendedPageRequest
    ): PageResponse<EventShortResponseForAdmin> {
        val events = eventDao.findByTitleStartsWithAndTagStartsWith(query, tag, pageRequest.pageable)
        return eventMapper.asPageResponseForAdmin(events)
    }

    /**
     * Returns list of 5 latest [EventResponsePreview]
     *
     * @return A list of [EventResponsePreview]
     */
    override fun previewEvents(): List<EventResponsePreview> {
        val page = PageRequest.of(0, 5, Sort.Direction.DESC, "createdAt")
        val events = eventDao.findByTimeAfterAndPublicationDateIsBeforeAndHiddenFalse(LocalDateTime.now(), page)
        return events.map { eventMapper.asPreviewResponse(it) }
    }

    override fun exportParticipants(eventId: UUID) = joinDao.findByEventId(eventId)

    /**
     * Creates a new publication in a specified feed.
     *
     * @param userId The ID of the user creating the publication.
     * @param feedId The ID of the feed in which to create the publication.
     * @param request The [PublicationRequest] containing the details of the publication to create.
     * @return The newly created [PublicationResponseForAdmin].
     */
    override fun post(userId: Long, feedId: Long, request: PublicationRequest): PublicationResponseForAdmin {
        publicationHelper.validatePublicationCreate(feedId, request)
        val publication = publicationMapper.asEntity(request).apply {
            feed = feedService.findPublicationFeedEntityById(feedId)
            author = userService.findUserEntityById(userId)
            publicationDao.save(this)
        }
        contentPhotoService.queueContent(ContentPhotoRequest(publication.id, publication.content))
        publicationHelper.sendBroadcastForPublication(userId, request.broadcast, publication)
        return publicationMapper.asResponseForAdmin(publication)
    }

    /**
     * Creates a new event in a specified feed.
     *
     * @param userId The ID of the user creating the event.
     * @param feedId The ID of the feed in which to create the event.
     * @param request The [EventRequest] containing the details of the event to create.
     * @return The newly created [EventResponseForUser].
     */
    override fun post(userId: Long, feedId: Long, request: EventRequest): EventResponseForAdmin {
        publicationHelper.validateEventCreate(feedId, request)
        val eventFeed = feedService.findEventFeedEntityById(feedId)
        val user = userService.findUserEntityById(userId)
        val event = eventMapper.asEntity(request).apply {
            feed = eventFeed
            author = user
        }
        eventDao.save(event)
        contentPhotoService.queueContent(ContentPhotoRequest(event.id, event.content))
        publicationHelper.sendBroadcastForEvent(userId, request.broadcast, event)
        return eventMapper.asResponseForAdmin(event)
    }

    /**
     * Updates an existing [Publication].
     *
     * @param feedId The ID of the feed associated with the publication to update.
     * @param publicationId The ID of the publication to update.
     * @param request The [PublicationUpdateRequest] containing the data to use to update the publication.
     * @return The updated [PublicationResponseForAdmin].
     * @throws ApiError if a publication already exists with the same human URL.
     */
    @Modifying
    override fun update(
        feedId: Long, publicationId: UUID, request: PublicationUpdateRequest
    ): PublicationResponseForAdmin {
        if (publicationDao.existsByHumanUrlAndIdNot(request.humanUrl, publicationId))
            throw ApiError(HttpStatus.CONFLICT, i18n("exception.alreadyExists.publication"), request.humanUrl)
        val publication = publicationHelper.findPublicationEntity(feedId, publicationId, true)
        publicationMapper.update(publication, request)
        contentPhotoService.queueContent(ContentPhotoRequest(publication.id, publication.content))
        return publicationMapper.asResponseForAdmin(publication)
    }

    /**
     * Updates an existing [Event].
     *
     * @param feedId The ID of the feed associated with the event to update.
     * @param eventId The ID of the event to update.
     * @param request The [EventUpdateRequest] containing the data to use to update the event.
     * @return The updated [EventResponseForAdmin].
     * @throws ApiError if an event already exists with the same human URL.
     */
    @Modifying
    override fun update(feedId: Long, eventId: UUID, request: EventUpdateRequest): EventResponseForAdmin {
        if (eventDao.existsByHumanUrlAndIdNot(request.humanUrl, eventId))
            throw ApiError(HttpStatus.CONFLICT, i18n("exception.alreadyExists.publication"), request.humanUrl)
        val event = publicationHelper.findEventEntity(feedId, eventId, true)
        eventMapper.update(event, request)
        contentPhotoService.queueContent(ContentPhotoRequest(event.id, event.content))
        return eventMapper.asResponseForAdmin(event)
    }

    /**
     * Uploads a photo to the publication with the given [publicationId].
     *
     * @param feedId The ID of the feed to which the publication belongs.
     * @param publicationId The ID of the publication to which to upload the photo.
     * @param file The photo to upload.
     * @return The updated [PublicationResponseForAdmin].
     */
    @Modifying
    override fun uploadPublicationPhoto(feedId: Long, publicationId: UUID, file: Part): PublicationResponseForAdmin {
        val publication = publicationHelper.findPublicationEntity(feedId, publicationId, true)
        publication.photoPath = storageManager.saveImage(file, StoreDir.PUBLICATION, publication.photoPath, true)
        return publicationMapper.asResponseForAdmin(publication)
    }

    /**
     * Uploads a photo to the event with the given [eventId].
     *
     * @param feedId The ID of the feed to which the event belongs.
     * @param eventId The ID of the event to which to upload the photo.
     * @param file The photo to upload.
     * @return The updated [EventResponseForAdmin].
     */
    @Modifying
    override fun uploadEventPhoto(feedId: Long, eventId: UUID, file: Part): EventResponseForAdmin {
        val event = publicationHelper.findEventEntity(feedId, eventId, true)
        event.photoPath = storageManager.saveImage(file, StoreDir.EVENT, event.photoPath, true)
        return eventMapper.asResponseForAdmin(event)
    }

    /**
     * Deletes a publication by [publicationId].
     *
     * @param feedId The ID of the feed that contains the publication.
     * @param publicationId The ID of the publication to delete.
     */
    @Modifying
    @CacheEvict(value = [LIKES_COUNT_CACHE], key = "#publicationId")
    override fun deletePublication(feedId: Long, publicationId: UUID) {
        val publication = publicationHelper.findPublicationEntity(feedId, publicationId, true)
        publicationDao.deleteById(publication.id)
        storageManager.removeImage(publication.photoPath, StoreDir.PUBLICATION)
    }

    /**
     * Deletes an event by [eventId].
     *
     * @param feedId The ID of the feed that contains the publication.
     * @param eventId The ID of the event to delete.
     */
    @Modifying
    @CacheEvict(value = [LIKES_COUNT_CACHE], key = "#eventId")
    override fun deleteEvent(feedId: Long, eventId: UUID) {
        val event = publicationHelper.findEventEntity(feedId, eventId, true)
        eventDao.deleteById(event.id)
        storageManager.removeImage(event.photoPath, StoreDir.EVENT)
    }

    /**
     * Retrieves a paginated list of likes for a publication or an event.
     *
     * @param feedId The ID of the feed associated with the publication or event.
     * @param publicationId The ID of the publication or event.
     * @param pageRequest The [ExtendedPageRequest] used to paginate the list of likes.
     * @return A [PageResponse] of [LikeResponse].
     */
    override fun listLikes(
        feedId: Long, publicationId: UUID, pageRequest: ExtendedPageRequest
    ): PageResponse<LikeResponse> {
        val publication = if (feedService.existsPublicationFeedById(feedId)) {
            publicationHelper.findPublicationEntity(feedId, publicationId, true)
        } else publicationHelper.findEventEntity(feedId, publicationId, true)
        val likes = likeDao.findByPublicationId(publication.id, pageRequest.pageable)
        return likeMapper.asPageResponse(likes)
    }

    /**
     * Allows a user to like or dislike a publication or event.
     *
     * @param userId The ID of the user who is liking or disliking the publication or event.
     * @param feedId The ID of the feed associated with the publication or event.
     * @param publicationId The ID of the publication or event.
     * @param request The [LikeRequest] indicating whether to like or dislike the publication or event.
     * @return The [LikesCountResponse] containing the total number of likes for the publication or event.
     */
    @Modifying
    override fun like(userId: Long, feedId: Long, publicationId: UUID, request: LikeRequest): LikesCountResponse {
        val user = userService.findUserEntityById(userId)
        val likesCount = try {
            val publication = findPublicationEntity(publicationId)
            if (request.like) likeService.like(publication, user) else likeService.dislike(publication, user)
        } catch (throwable: ResourceNotFoundException) {
            val event = findEventEntity(publicationId)
            if (request.like) likeService.like(event, user) else likeService.dislike(event, user)
        }
        return LikesCountResponse(likesCount)
    }

    /**
     * Retrieves a paginated list of joins for an event.
     *
     * @param feedId The ID of the feed associated with the event.
     * @param eventId The ID of the event.
     * @param pageRequest The [ExtendedPageRequest] used to paginate the list of joins.
     * @return A [PageResponse] of [JoinResponse].
     */
    override fun listJoins(feedId: Long, eventId: UUID, pageRequest: ExtendedPageRequest): PageResponse<JoinResponse> {
        val event = publicationHelper.findEventEntity(feedId, eventId, true)
        val joins = joinDao.findByEventId(event.id, pageRequest.pageable)
        return joinMapper.asPageResponse(joins)
    }

    /**
     * Registers or deletes a user's participation in an event.
     *
     * @param userId The ID of the user participating in the event.
     * @param feedId The ID of the feed that contains the event.
     * @param eventId The ID of the event.
     * @param request A [ParticipationDto] object containing a flag indicating whether to register or delete
     * the user's participation.
     * @return A [EventParticipationResponse] object containing the number of users who have joined the event.
     * @throws ApiError with [HttpStatus.FORBIDDEN] if the event's registration is closed or if external registration
     * is required and the user attempts to register.
     */
    @Modifying
    override fun participate(
        userId: Long, feedId: Long, eventId: UUID, request: ParticipationDto
    ): EventParticipationResponse {
        val user = userService.findUserEntityById(userId)
        val event = publicationHelper.findEventEntity(feedId, eventId)
        if (!event.registrationIsOpen)
            throw ApiError(HttpStatus.FORBIDDEN, i18n("exception.feed.event.participateClosed"))
        if (event.externalRegistrationLink != null && request.participation)
            throw ApiError(HttpStatus.FORBIDDEN, i18n("exception.feed.event.externalRegistrationRequired"))
        if (request.participation) joinService.join(event, user) else joinService.leave(event, user)
        return EventParticipationResponse(joinService.existsByEventAndUserId(event, userId))
    }

    /**
     * Registers a user for a meeting.
     *
     * @param userId The ID of the user joining the meeting.
     * @param eventId The ID of the meeting event.
     * @param request The meeting participation request containing information about the user's participation.
     * @return The [JoinResponse] containing information about the user's meeting participation.
     * @throws ApiError with HTTP status code FORBIDDEN if the meeting registration is closed or the meeting
     * does not have an external registration link.
     */
    override fun joinMeeting(userId: Long, eventId: UUID, request: MeetingParticipationRequest): JoinResponse {
        val user = userService.findUserEntityById(userId)
        val event = findEventEntity(eventId)
        if (!event.registrationIsOpen)
            throw ApiError(HttpStatus.FORBIDDEN, i18n("exception.feed.event.participateClosed"))
        if (event.externalRegistrationLink == null)
            throw ApiError(HttpStatus.FORBIDDEN, i18n("exception.forbidden.eventExternalRegistration"))
        val join = joinService.join(event, user, request)
        return joinMapper.asResponse(join)
    }

    /**
     * Allows a user to leave a meeting.
     *
     * @param userId The ID of the user who wants to leave the meeting.
     * @param eventId The ID of the event (meeting) the user wants to leave.
     */
    override fun leaveMeeting(userId: Long, eventId: UUID) {
        val user = userService.findUserEntityById(userId)
        val event = findEventEntity(eventId, ignoreChecks = true)
        joinService.leave(event, user)
    }


    /**
     * Returns a [AbstractPublication] ([Event] or [Publication]) for a given [id].
     *
     * @param id The ID of the [AbstractPublication] to retrieve.
     * @return A [AbstractPublication] that match the provided [id] or null if it not exists.
     */
    override fun getCommonPublication(id: UUID): AbstractPublication? {
        val publication = publicationDao.findById(id)
        if (publication.isPresent) return publication.get()
        val event = eventDao.findById(id)
        if (event.isPresent) return event.get()
        return null
    }

    /**
     * Changes the hidden property of all publications in a feed.
     *
     * @param feedId The ID of the feed for which to change the hidden property of all its publications.
     * @param hidden The new hidden property value to set for all publications in the feed.
     */
    override fun setHiddenToAllPublicationsInFeed(feedId: Long, hidden: Boolean) {
        publicationDao.setHiddenToAllPublicationsInFeed(feedId, hidden)
    }

    /**
     * Changes the hidden property of all events in a feed.
     *
     * @param feedId The ID of the feed for which to change the hidden property of all its events.
     * @param hidden The new hidden property value to set for all events in the feed.
     */
    override fun setHiddenToAllEventsInFeed(feedId: Long, hidden: Boolean) {
        eventDao.setHiddenToAllEventsInFeed(feedId, hidden)
    }

    /**
     * Returns a page of [Publication].
     *
     * @param page The [Pageable] used to filter and paginate the list of publications.
     * @return A [Page] of [Publication].
     */
    override fun findAllPublications(page: Pageable): Page<Publication> {
        return publicationDao.findAll(page)
    }
}
