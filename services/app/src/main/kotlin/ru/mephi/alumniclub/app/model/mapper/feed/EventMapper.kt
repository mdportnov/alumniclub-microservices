package ru.mephi.alumniclub.app.model.mapper.feed

import org.springframework.data.domain.Page
import org.springframework.stereotype.Component
import ru.mephi.alumniclub.app.database.entity.publication.Event
import ru.mephi.alumniclub.app.model.dto.CursorResponse
import ru.mephi.alumniclub.app.model.dto.PageResponse
import ru.mephi.alumniclub.app.model.dto.feed.request.EventRequest
import ru.mephi.alumniclub.app.model.dto.feed.request.EventUpdateRequest
import ru.mephi.alumniclub.app.model.dto.feed.response.admin.EventResponseForAdmin
import ru.mephi.alumniclub.app.model.dto.feed.response.admin.EventShortResponseForAdmin
import ru.mephi.alumniclub.app.model.dto.feed.response.user.EventResponseForUser
import ru.mephi.alumniclub.app.model.dto.feed.response.user.EventResponsePreview
import ru.mephi.alumniclub.app.model.dto.feed.response.user.EventShortResponseForUser
import ru.mephi.alumniclub.app.model.mapper.user.UserMapper
import ru.mephi.alumniclub.app.service.JoinService
import ru.mephi.alumniclub.app.service.LikeService
import ru.mephi.alumniclub.app.util.parseArticle

@Component
class EventMapper(
    private val feedMapper: FeedMapper,
    private val userMapper: UserMapper,
    private val likeService: LikeService,
    private val joinService: JoinService
) {
    fun asEntity(request: EventRequest): Event {
        return Event(
            title = request.title,
            content = request.content,
            humanUrl = request.humanUrl,
            place = request.place,
            time = request.time,
            tag = request.tag,
            publicationDate = request.publicationDate,
            externalRegistrationLink = request.externalRegistrationLink,
            hidden = request.hidden,
            registrationIsOpen = request.registrationIsOpen
        )
    }

    fun update(event: Event, request: EventUpdateRequest): Event {
        event.title = request.title
        event.content = request.content
        event.humanUrl = request.humanUrl
        event.place = request.place
        event.time = request.time
        event.tag = request.tag
        event.externalRegistrationLink = request.externalRegistrationLink
        event.hidden = request.hidden
        event.registrationIsOpen = request.registrationIsOpen
        event.publicationDate = request.publicationDate
        return event
    }

    fun asPreviewResponse(event: Event): EventResponsePreview {
        return EventResponsePreview(
            id = event.id,
            publicationDate = event.publicationDate,
            title = event.title,
            content = parseArticle(event.content),
            feed = feedMapper.asResponse(event.feed),
            humanUrl = event.humanUrl,
            time = event.time
        )
    }

    fun asResponseForPublic(event: Event): EventResponseForUser {
        return EventResponseForUser(
            id = event.id,
            publicationDate = event.publicationDate,
            title = event.title,
            content = event.content,
            humanUrl = event.humanUrl,
            place = event.place,
            feed = feedMapper.asResponse(event.feed),
            author = userMapper.asNullableShortResponse(event.author),
            time = event.time,
            liked = false,
            joined = false,
            likesCount = likeService.getLikesCount(event),
            viewsCount = event.viewsCount,
            tag = event.tag,
            externalRegistrationLink = event.externalRegistrationLink,
            registrationIsOpen = event.registrationIsOpen,
            photoPath = event.photoPath
        )
    }

    fun asResponseForUser(userId: Long, event: Event): EventResponseForUser {
        return EventResponseForUser(
            id = event.id,
            publicationDate = event.publicationDate,
            title = event.title,
            content = event.content,
            humanUrl = event.humanUrl,
            place = event.place,
            feed = feedMapper.asResponse(event.feed),
            author = userMapper.asNullableShortResponse(event.author),
            time = event.time,
            liked = likeService.existsByAbstractPublicationAndUserId(event, userId),
            joined = joinService.existsByEventAndUserId(event, userId),
            likesCount = likeService.getLikesCount(event),
            viewsCount = event.viewsCount,
            tag = event.tag,
            externalRegistrationLink = event.externalRegistrationLink,
            registrationIsOpen = event.registrationIsOpen,
            photoPath = event.photoPath
        )
    }

    fun asResponseForAdmin(event: Event): EventResponseForAdmin {
        return EventResponseForAdmin(
            id = event.id,
            createdAt = event.createdAt,
            publicationDate = event.publicationDate,
            title = event.title,
            content = event.content,
            humanUrl = event.humanUrl,
            place = event.place,
            author = userMapper.asNullableShortResponse(event.author),
            feed = feedMapper.asResponse(event.feed),
            time = event.time,
            likesCount = likeService.getLikesCount(event),
            viewsCount = event.viewsCount,
            joinsCount = joinService.getJoinsCount(event),
            tag = event.tag,
            externalRegistrationLink = event.externalRegistrationLink,
            hidden = event.hidden,
            registrationIsOpen = event.registrationIsOpen,
            photoPath = event.photoPath
        )
    }

    fun asPageResponseForUser(userId: Long, events: Page<Event>): PageResponse<EventShortResponseForUser> {
        return PageResponse(
            content = events.content.map { asShortResponseForUser(userId, it) },
            number = events.number.toLong(),
            numberOfElements = events.numberOfElements.toLong(),
            totalPages = events.totalPages.toLong()
        )
    }

    fun asCursorResponseForUser(userId: Long, events: Page<Event>): CursorResponse<EventShortResponseForUser> {
        return CursorResponse(
            content = events.content.map { asShortResponseForUser(userId, it) },
            numberOfElements = events.numberOfElements.toLong()
        )
    }

    fun asPageResponseForAdmin(events: Page<Event>): PageResponse<EventShortResponseForAdmin> {
        return PageResponse(
            content = events.content.map(::asShortResponseForAdmin),
            number = events.number.toLong(),
            numberOfElements = events.numberOfElements.toLong(),
            totalPages = events.totalPages.toLong()
        )
    }

    private fun asShortResponseForUser(userId: Long, event: Event): EventShortResponseForUser {
        return EventShortResponseForUser(
            id = event.id,
            publicationDate = event.publicationDate,
            title = event.title,
            humanUrl = event.humanUrl,
            place = event.place,
            feed = feedMapper.asResponse(event.feed),
            content = parseArticle(event.content),
            liked = likeService.existsByAbstractPublicationAndUserId(event, userId),
            joined = joinService.existsByEventAndUserId(event, userId),
            viewsCount = event.viewsCount,
            likesCount = likeService.getLikesCount(event),
            time = event.time,
            tag = event.tag,
            externalRegistrationLink = event.externalRegistrationLink,
            registrationIsOpen = event.registrationIsOpen,
            photoPath = event.photoPath
        )
    }

    private fun asShortResponseForAdmin(event: Event): EventShortResponseForAdmin {
        return EventShortResponseForAdmin(
            id = event.id,
            createdAt = event.createdAt,
            publicationDate = event.publicationDate,
            title = event.title,
            humanUrl = event.humanUrl,
            place = event.place,
            feed = feedMapper.asResponse(event.feed),
            content = event.content,
            likesCount = likeService.getLikesCount(event),
            viewsCount = event.viewsCount,
            joinsCount = joinService.getJoinsCount(event),
            time = event.time,
            tag = event.tag,
            externalRegistrationLink = event.externalRegistrationLink,
            hidden = event.hidden,
            registrationIsOpen = event.registrationIsOpen,
            photoPath = event.photoPath
        )
    }
}
