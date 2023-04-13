package ru.mephi.alumniclub.app.service

import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RequestPart
import ru.mephi.alumniclub.app.database.entity.publication.AbstractPublication
import ru.mephi.alumniclub.app.database.entity.publication.Event
import ru.mephi.alumniclub.app.database.entity.publication.Join
import ru.mephi.alumniclub.app.database.entity.publication.Publication
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
import ru.mephi.alumniclub.shared.dto.ExtendedPageRequest
import ru.mephi.alumniclub.shared.util.Cursor
import java.util.*
import javax.servlet.http.Part

@Tag(name = "Publication API")
interface PublicationService {

    fun exportParticipants(eventId: UUID): List<Join>

    fun listPublicationsByFeedIdForUser(
        userId: Long,
        @PathVariable feedId: Long,
        query: String = "",
        pageRequest: ExtendedPageRequest,
    ): PageResponse<PublicationShortResponseForUser>

    fun listPublicationsByFeedIdForAdmin(
        @PathVariable feedId: Long,
        query: String = "",
        pageRequest: ExtendedPageRequest,
    ): PageResponse<PublicationShortResponseForAdmin>

    fun listEventsByFeedIdForUser(
        userId: Long,
        @PathVariable feedId: Long,
        query: String = "",
        tag: String = "",
        pageRequest: ExtendedPageRequest,
    ): PageResponse<EventShortResponseForUser>

    fun listEventsByFeedIdForAdmin(
        @PathVariable feedId: Long,
        query: String = "",
        tag: String = "",
        pageRequest: ExtendedPageRequest,
    ): PageResponse<EventShortResponseForAdmin>

    fun listPublicationsByFeedIdForPublic(
        @PathVariable feedId: Long,
        pageRequest: ExtendedPageRequest,
    ): PageResponse<PublicationShortResponseForUser>

    fun listPublicationsByFeedIdForUser(
        @PathVariable feedId: Long,
        @Parameter(hidden = true) userId: Long,
        cursor: Cursor,
    ): CursorResponse<PublicationShortResponseForUser>

    fun listEventsByFeedIdForUser(
        @PathVariable feedId: Long,
        @Parameter(hidden = true) userId: Long,
        cursor: Cursor,
    ): CursorResponse<EventShortResponseForUser>

    fun listAllPublications(
        query: String,
        pageRequest: ExtendedPageRequest
    ): PageResponse<PublicationShortResponseForUser>

    fun listAllEventsForUser(
        @Parameter(hidden = true) userId: Long,
        cursor: Cursor
    ): CursorResponse<EventShortResponseForUser>

    fun listAllEventsForAdmin(
        query: String, tag: String, pageRequest: ExtendedPageRequest
    ): PageResponse<EventShortResponseForAdmin>

    fun previewEvents(): List<EventResponsePreview>
    fun findPublicationEntity(id: UUID, ignoreChecks: Boolean = false): Publication
    fun findEventEntity(id: UUID, ignoreChecks: Boolean = false): Event
    fun findPublicationByIdForPublic(
        @PathVariable id: UUID,
        withRecommendation: Boolean = false
    ): PublicationResponseForUser

    fun findPublicationByHumanUrl(@PathVariable url: String): PublicationResponseForUser
    fun findEventByHumanUrl(url: String): EventResponseForUser
    fun findPublicationByIdForUser(
        @Parameter(hidden = true) userId: Long,
        @PathVariable feedId: Long,
        @PathVariable publicationId: UUID,
        @RequestParam withRecommendation: Boolean = false
    ): PublicationResponseForUser

    fun findPublicationByIdForAdmin(
        @PathVariable feedId: Long,
        @PathVariable publicationId: UUID,
    ): PublicationResponseForAdmin

    fun findEventByIdForUser(
        @Parameter(hidden = true) userId: Long,
        @PathVariable feedId: Long,
        @PathVariable eventId: UUID,
    ): EventResponseForUser

    fun findEventByIdForPublic(
        @PathVariable eventId: UUID,
    ): EventResponseForUser

    fun findEventByIdForAdmin(
        @PathVariable feedId: Long,
        @PathVariable eventId: UUID,
    ): EventResponseForAdmin

    fun post(
        @Parameter(hidden = true) userId: Long,
        @PathVariable feedId: Long,
        @RequestBody request: PublicationRequest,
    ): PublicationResponseForAdmin

    fun post(
        @Parameter(hidden = true) userId: Long,
        @PathVariable feedId: Long,
        @RequestBody request: EventRequest,
    ): EventResponseForAdmin

    fun update(
        @PathVariable feedId: Long,
        @PathVariable publicationId: UUID,
        @RequestBody request: PublicationUpdateRequest,
    ): PublicationResponseForAdmin

    fun update(
        @PathVariable feedId: Long,
        @PathVariable eventId: UUID,
        @RequestBody request: EventUpdateRequest,
    ): EventResponseForAdmin

    fun uploadPublicationPhoto(
        @PathVariable feedId: Long,
        @PathVariable publicationId: UUID,
        @RequestPart file: Part,
    ): PublicationResponseForAdmin

    fun uploadEventPhoto(
        @PathVariable feedId: Long,
        @PathVariable eventId: UUID,
        @RequestPart file: Part,
    ): EventResponseForAdmin

    fun deletePublication(
        @PathVariable feedId: Long,
        @PathVariable publicationId: UUID,
    )

    fun deleteEvent(
        @PathVariable feedId: Long,
        @PathVariable eventId: UUID,
    )

    fun like(
        @Parameter(hidden = true) userId: Long,
        @PathVariable feedId: Long,
        @PathVariable publicationId: UUID,
        @RequestBody request: LikeRequest,
    ): LikesCountResponse

    fun listLikes(
        @PathVariable feedId: Long,
        @PathVariable publicationId: UUID,
        pageRequest: ExtendedPageRequest,
    ): PageResponse<LikeResponse>

    fun listJoins(
        @PathVariable feedId: Long,
        @PathVariable eventId: UUID,
        pageRequest: ExtendedPageRequest,
    ): PageResponse<JoinResponse>

    fun participate(
        @Parameter(hidden = true) userId: Long,
        @PathVariable feedId: Long,
        @PathVariable eventId: UUID,
        @RequestBody request: ParticipationDto,
    ): EventParticipationResponse

    fun joinMeeting(
        @Parameter(hidden = true) userId: Long,
        @PathVariable eventId: UUID,
        @RequestBody request: MeetingParticipationRequest,
    ): JoinResponse

    fun leaveMeeting(
        @Parameter(hidden = true) userId: Long, @PathVariable eventId: UUID
    )

    fun getCommonPublication(id: UUID): AbstractPublication?

    fun setHiddenToAllPublicationsInFeed(feedId: Long, hidden: Boolean)
    fun setHiddenToAllEventsInFeed(feedId: Long, hidden: Boolean)
    fun findAllPublications(page: Pageable): Page<Publication>
}
