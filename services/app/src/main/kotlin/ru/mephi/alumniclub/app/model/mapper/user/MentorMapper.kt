package ru.mephi.alumniclub.app.model.mapper.user

import org.springframework.data.domain.Page
import org.springframework.stereotype.Component
import ru.mephi.alumniclub.app.database.entity.user.MentorData
import ru.mephi.alumniclub.app.database.entity.user.User
import ru.mephi.alumniclub.app.model.dto.PageResponse
import ru.mephi.alumniclub.app.model.dto.mentor.request.CreateMentorRequest
import ru.mephi.alumniclub.app.model.dto.mentor.request.UpdateMentorRequest
import ru.mephi.alumniclub.app.model.dto.mentor.response.MentorFullResponse
import ru.mephi.alumniclub.app.model.dto.mentor.response.MentorShortResponse

@Component
class MentorMapper {
    fun asEntity(user: User, request: CreateMentorRequest): MentorData {
        return MentorData(
            user = user,
            company = request.company,
            position = request.position,
            expertArea = request.expertArea,
            whyAreYouMentor = request.whyAreYouMentor,
            graduation = request.graduation,
            formatsOfInteractions = request.formatsOfInteractions,
            tags = request.tags,
            interviewLink = request.interviewLink,
        )
    }

    fun update(entity: MentorData, request: UpdateMentorRequest, userId: Long): MentorData {
        entity.company = request.company
        entity.position = request.position
        entity.expertArea = request.expertArea
        entity.whyAreYouMentor = request.whyAreYouMentor
        entity.graduation = request.graduation
        entity.formatsOfInteractions = request.formatsOfInteractions
        entity.tags = request.tags
        entity.interviewLink = request.interviewLink
        entity.userId = userId
        return entity
    }

    fun asShortPageResponse(entity: MentorData): MentorShortResponse {
        return MentorShortResponse(
            name = entity.user.name,
            available = entity.available,
            surname = entity.user.surname,
            photoPath = entity.user.photoPath,
            userId = entity.user.id,
            company = entity.company,
            position = entity.position,
            graduation = entity.graduation,
            tags = entity.tags,
            interviewLink = entity.interviewLink,
        )
    }

    fun asShortPageResponse(entities: Page<MentorData>): PageResponse<MentorShortResponse> {
        val content: List<MentorShortResponse> = mentorDataListToMentorShortResponseList(entities.content)
        return PageResponse(
            content,
            entities.number.toLong(),
            entities.numberOfElements.toLong(),
            entities.totalPages.toLong()
        )
    }

    fun mentorDataListToMentorShortResponseList(list: List<MentorData>): List<MentorShortResponse> =
        list.map(::asShortPageResponse)

    fun asFullResponse(entity: MentorData): MentorFullResponse {
        return MentorFullResponse(
            company = entity.company,
            position = entity.position,
            expertArea = entity.expertArea,
            whyAreYouMentor = entity.whyAreYouMentor,
            graduation = entity.graduation,
            formatsOfInteractions = entity.formatsOfInteractions,
            tags = entity.tags,
            available = entity.available,
            surname = entity.user.surname,
            name = entity.user.name,
            patronymic = entity.user.patronymic,
            photoPath = entity.user.photoPath,
            userId = entity.user.id,
            interviewLink = entity.interviewLink,
        )
    }
}
