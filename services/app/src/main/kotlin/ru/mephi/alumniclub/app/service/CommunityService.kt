package ru.mephi.alumniclub.app.service

import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import ru.mephi.alumniclub.app.database.entity.community.Community
import ru.mephi.alumniclub.app.database.entity.project.AbstractProject
import ru.mephi.alumniclub.app.database.entity.user.User
import ru.mephi.alumniclub.app.model.dto.CursorResponse
import ru.mephi.alumniclub.app.model.dto.PageResponse
import ru.mephi.alumniclub.app.model.dto.ParticipationDto
import ru.mephi.alumniclub.app.model.dto.community.request.CommunityRequest
import ru.mephi.alumniclub.app.model.dto.community.response.CommunityResponse
import ru.mephi.alumniclub.app.model.dto.community.response.MemberResponse
import ru.mephi.alumniclub.app.model.enumeration.user.Role
import ru.mephi.alumniclub.shared.dto.ExtendedPageRequest
import ru.mephi.alumniclub.shared.util.Cursor
import javax.servlet.http.Part

@Tag(name = "Community API")
interface CommunityService {
    fun existById(communityId: Long): Boolean
    fun list(query: String = "", pageRequest: ExtendedPageRequest): PageResponse<CommunityResponse>
    fun listByUser(
        @Parameter(hidden = true) userId: Long,
        pageRequest: ExtendedPageRequest,
        @Parameter(hidden = true) includeHidden: Boolean = false
    ): PageResponse<CommunityResponse>

    fun listByUserNot(@Parameter(hidden = true) userId: Long, cursor: Cursor): CursorResponse<CommunityResponse>
    fun listMembersForUser(@PathVariable id: Long, query: String, pageRequest: ExtendedPageRequest): PageResponse<MemberResponse>
    fun listMembersForAdmin(@PathVariable id: Long, query: String, pageRequest: ExtendedPageRequest): PageResponse<MemberResponse>

    fun findEntityById(id: Long): Community
    fun findById(@PathVariable id: Long, @Parameter(hidden = true) includeHidden: Boolean = false): CommunityResponse

    fun create(project: AbstractProject): Community
    fun create(@RequestBody request: CommunityRequest): CommunityResponse

    fun update(@PathVariable id: Long, @RequestBody request: CommunityRequest): CommunityResponse
    fun update(community: Community, name: String, hidden: Boolean): Community
    fun updateRolesCommunities(user: User, roles: Set<Role>)
    fun uploadPhoto(@PathVariable id: Long, file: Part): CommunityResponse
    fun hide(community: Community)
    fun show(community: Community)
    fun delete(@PathVariable id: Long)

    fun participate(
        @Parameter(hidden = true) userId: Long,
        @PathVariable communityId: Long,
        @RequestBody request: ParticipationDto,
        allowProject: Boolean = false
    ): MemberResponse?
}
