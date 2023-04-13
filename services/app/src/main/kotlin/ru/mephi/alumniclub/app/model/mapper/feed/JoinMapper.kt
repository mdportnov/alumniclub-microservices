package ru.mephi.alumniclub.app.model.mapper.feed

import org.springframework.data.domain.Page
import org.springframework.stereotype.Component
import ru.mephi.alumniclub.app.database.entity.feed.MeetingParticipation
import ru.mephi.alumniclub.app.database.entity.publication.Join
import ru.mephi.alumniclub.app.model.dto.PageResponse
import ru.mephi.alumniclub.app.model.dto.feed.response.JoinResponse
import ru.mephi.alumniclub.app.model.dto.meeting.MeetingParticipationRequest
import ru.mephi.alumniclub.app.model.mapper.user.UserMapper

@Component
class JoinMapper(
    private val userMapper: UserMapper
) {
    fun asEntity(join: Join, request: MeetingParticipationRequest): MeetingParticipation {
        return MeetingParticipation(
            join = join,
            format = request.format,
            departmentMeetup = request.departmentMeetup,
            movie = request.movie,
            presentations = request.presentations,
            exhibition = request.exhibition,
            greetings = request.greetings,
            enjoy = request.enjoy,
            performance = request.performance,
            help = request.help,
            telegram = request.telegram
        )
    }

    fun asResponse(join: Join): JoinResponse {
        return JoinResponse(
            id = join.id,
            createdAt = join.createdAt,
            user = userMapper.asPreviewResponse(join.user)
        )
    }

    fun asPageResponse(joins: Page<Join>): PageResponse<JoinResponse> {
        return PageResponse(
            content = joins.content.map(::asResponse),
            number = joins.number.toLong(),
            numberOfElements = joins.numberOfElements.toLong(),
            totalPages = joins.totalPages.toLong()
        )
    }
}
