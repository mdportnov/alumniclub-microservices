package ru.mephi.alumniclub.app.model.mapper.community

import org.springframework.data.domain.Page
import org.springframework.stereotype.Component
import ru.mephi.alumniclub.app.database.entity.community.Community
import ru.mephi.alumniclub.app.model.dto.CursorResponse
import ru.mephi.alumniclub.app.model.dto.PageResponse
import ru.mephi.alumniclub.app.model.dto.community.request.CommunityRequest
import ru.mephi.alumniclub.app.model.dto.community.response.CommunityResponse

@Component
class CommunityMapper {
    fun asEntity(name: String, role: Boolean = false): Community {
        return Community(
            name = name,
            role = role
        )
    }

    fun update(community: Community, request: CommunityRequest): Community {
        community.name = request.name
        community.hidden = request.hidden
        return community
    }

    fun asResponse(community: Community): CommunityResponse {
        return CommunityResponse(
            id = community.id,
            createdAt = community.createdAt,
            name = community.name,
            membersCount = community.membersCount,
            hidden = community.hidden,
            projectId = community.project?.id,
            role = community.role,
            photoPath = community.photoPath
        )
    }

    fun asPageResponse(page: Page<Community>): PageResponse<CommunityResponse> {
        val content = page.content.map(::asResponse)
        return PageResponse(
            content = content,
            number = page.number.toLong(),
            numberOfElements = page.numberOfElements.toLong(),
            totalPages = page.totalPages.toLong()
        )
    }

    fun asCursorResponse(page: Page<Community>): CursorResponse<CommunityResponse> {
        val content = page.content.map(::asResponse)
        return CursorResponse(
            content = content,
            numberOfElements = page.numberOfElements.toLong()
        )
    }
}
