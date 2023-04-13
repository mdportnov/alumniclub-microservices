package ru.mephi.alumniclub.app.model.mapper.partnership

import org.springframework.data.domain.Page
import org.springframework.stereotype.Component
import ru.mephi.alumniclub.app.database.entity.form.FormJoinPartnership
import ru.mephi.alumniclub.app.database.entity.partnership.Partnership
import ru.mephi.alumniclub.app.database.entity.user.User
import ru.mephi.alumniclub.app.model.dto.PageResponse
import ru.mephi.alumniclub.app.model.dto.partnership.request.PartnershipRequest
import ru.mephi.alumniclub.app.model.dto.partnership.response.CreatorResponse
import ru.mephi.alumniclub.app.model.dto.partnership.response.PartnershipJoinResponse
import ru.mephi.alumniclub.app.model.dto.partnership.response.PartnershipResponse
import ru.mephi.alumniclub.app.model.mapper.user.UserMapper
import ru.mephi.alumniclub.app.service.UserService

@Component
class PartnershipMapper(
    private val userService: UserService,
    private val userMapper: UserMapper
) {
    fun asEntity(request: PartnershipRequest, authorId: Long): Partnership {
        return Partnership(
            tag = request.tag,
            color = request.color,
            projectName = request.projectName,
            aboutProject = request.aboutProject,
            helpDescription = request.helpDescription,
            currentUntil = request.currentUntil,
            creatorDescription = request.creatorDescription,
        ).apply {
            author = userService.findUserEntityById(authorId)
            creator = userService.findUserEntityById(request.creatorId)
        }
    }

    fun asPartnershipResponse(partnership: Partnership): PartnershipResponse {
        val creator = asCreatorResponse(
            user = partnership.creator,
            description = partnership.creatorDescription
        )
        return PartnershipResponse(
            id = partnership.id,
            createdAt = partnership.createdAt,
            tag = partnership.tag,
            color = partnership.color,
            projectName = partnership.projectName,
            aboutProject = partnership.aboutProject,
            helpDescription = partnership.helpDescription,
            currentUntil = partnership.currentUntil,
            creator = creator,
            photoPath = partnership.photoPath
        )
    }

    fun asPartnershipPageResponse(page: Page<Partnership>): PageResponse<PartnershipResponse> {
        val content = page.content.map(::asPartnershipResponse)
        return PageResponse(
            content = content,
            number = page.number.toLong(),
            numberOfElements = page.numberOfElements.toLong(),
            totalPages = page.totalPages.toLong()
        )
    }

    fun asMemberPageResponse(page: Page<FormJoinPartnership>): PageResponse<PartnershipJoinResponse> {
        val content = page.content.map(::asMemberResponse)
        return PageResponse(
            content = content,
            number = page.number.toLong(),
            numberOfElements = page.numberOfElements.toLong(),
            totalPages = page.totalPages.toLong()
        )
    }

    fun update(partnership: Partnership, request: PartnershipRequest): Partnership {
        partnership.tag = request.tag
        partnership.color = request.color
        partnership.projectName = request.projectName
        partnership.aboutProject = request.aboutProject
        partnership.helpDescription = request.helpDescription
        partnership.currentUntil = request.currentUntil
        partnership.creatorDescription = request.creatorDescription
        partnership.creator = userService.findUserEntityById(request.creatorId)
        return partnership
    }

    private fun asCreatorResponse(user: User, description: String): CreatorResponse {
        return CreatorResponse(
            id = user.id,
            createdAt = user.createdAt,
            name = user.name,
            surname = user.surname,
            creatorDescription = description,
            photoPath = user.photoPath,
        )
    }

    private fun asMemberResponse(form: FormJoinPartnership): PartnershipJoinResponse {
        return PartnershipJoinResponse(
            id = form.id,
            member = userMapper.asShortResponse(form.author!!),
            partnership = asPartnershipResponse(form.partnership),
            contribution = form.contribution
        )
    }
}