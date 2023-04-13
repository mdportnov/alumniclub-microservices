package ru.mephi.alumniclub.app.service.impl

import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import ru.mephi.alumniclub.app.database.entity.publication.Event
import ru.mephi.alumniclub.app.database.entity.publication.Join
import ru.mephi.alumniclub.app.database.entity.user.User
import ru.mephi.alumniclub.app.database.repository.feed.JoinDao
import ru.mephi.alumniclub.app.model.dto.meeting.MeetingParticipationRequest
import ru.mephi.alumniclub.app.model.mapper.feed.JoinMapper
import ru.mephi.alumniclub.app.service.JoinService
import ru.mephi.alumniclub.shared.dto.common.ApiError
import ru.mephi.alumniclub.shared.util.response.ResponseManager
import java.time.LocalDateTime
import javax.transaction.Transactional

@Service
@Transactional
class JoinServiceImpl(
    private val mapper: JoinMapper,
    private val joinDao: JoinDao
) : ResponseManager(), JoinService {
    /**
     * Joins the given [User] to the [Event].
     *
     * @param event the [Event] to join
     * @param user the [User] joining the event
     * @param request the [MeetingParticipationRequest] with the meeting participation information
     * @throws ApiError with [HttpStatus.FORBIDDEN] if the [User] has already joined the [Event]
     * @throws ApiError with [HttpStatus.FORBIDDEN] if the [Event] has already taken place
     * @return the [Join] of the [User] and [Event]
     */
    override fun join(event: Event, user: User): Join {
        if (existsByEventAndUserId(event, user.id))
            throw ApiError(HttpStatus.FORBIDDEN, i18n("exception.alreadyExists.join"))
        if (event.time < LocalDateTime.now())
            throw ApiError(HttpStatus.FORBIDDEN, i18n("exception.forbidden.eventParticipatePast"))
        val join = Join(user, event.id)
        return joinDao.save(join)
    }

    /**
     * Joins the given [User] to the [Event] and registers for the alumni meeting.
     *
     * @param event the [Event] to join
     * @param user the [User] joining the event
     * @param request the [MeetingParticipationRequest] with the information about alumni meeting
     * @return the [Join] of the [User] and [Event]
     */
    override fun join(event: Event, user: User, request: MeetingParticipationRequest): Join {
        val join = join(event, user)
        join.meetingParticipation = mapper.asEntity(join, request)
        return join
    }

    /**
     * Leaves the given [Event] for the [User].
     *
     * @param event the [Event] to leave
     * @param user the [User] leaving the event
     * @throws ApiError with a HTTP status of 404 if the [User] has not joined the [Event]
     * @throws ApiError with a HTTP status of 403 if the [Event] has already taken place
     * @return the count of remaining joins for the [Event]
     */
    override fun leave(event: Event, user: User): Long {
        if (!existsByEventAndUserId(event, user.id))
            throw ApiError(HttpStatus.NOT_FOUND, i18n("exception.feed.event.notJoined"))
        if (event.time < LocalDateTime.now())
            throw ApiError(HttpStatus.FORBIDDEN, i18n("exception.forbidden.eventParticipatePast"))
        joinDao.deleteByEventIdAndUser(event.id, user)
        return getJoinsCount(event)
    }

    /**
     * Gets the count of joins for the given [Event].
     *
     * @param event the [Event] to get join count for
     * @return the count of joins for the [Event]
     */
    override fun getJoinsCount(event: Event): Long {
        return joinDao.countByEventId(event.id)
    }

    /**
     * Determines if a [Join] exists for the given [Event] and [User].
     *
     * @param event the [Event] to check for join
     * @param userId the ID of the [User] to check for join
     * @return `true` if a join exists, `false` otherwise
     */
    override fun existsByEventAndUserId(event: Event, userId: Long): Boolean {
        return joinDao.existsByEventIdAndUserId(event.id, userId)
    }
}
