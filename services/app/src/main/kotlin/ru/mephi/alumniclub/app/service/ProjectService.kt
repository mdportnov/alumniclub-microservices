package ru.mephi.alumniclub.app.service

import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestPart
import ru.mephi.alumniclub.app.database.entity.feed.AbstractFeed
import ru.mephi.alumniclub.app.database.entity.project.AbstractProject
import ru.mephi.alumniclub.app.database.entity.publication.AbstractPublication
import ru.mephi.alumniclub.app.model.dto.CursorResponse
import ru.mephi.alumniclub.app.model.dto.PageResponse
import ru.mephi.alumniclub.app.model.dto.ParticipationDto
import ru.mephi.alumniclub.app.model.dto.community.response.MemberResponse
import ru.mephi.alumniclub.app.model.dto.project.ArchiveDto
import ru.mephi.alumniclub.app.model.dto.project.request.ProjectCreateRequest
import ru.mephi.alumniclub.app.model.dto.project.request.ProjectRequest
import ru.mephi.alumniclub.app.model.dto.project.response.ProjectPreviewResponse
import ru.mephi.alumniclub.app.model.dto.project.response.ProjectPublicShortResponse
import ru.mephi.alumniclub.app.model.dto.project.response.ProjectResponse
import ru.mephi.alumniclub.app.model.dto.project.response.ProjectShortResponse
import ru.mephi.alumniclub.app.model.enumeration.ProjectStatus
import ru.mephi.alumniclub.shared.dto.ExtendedPageRequest
import ru.mephi.alumniclub.shared.util.Cursor
import javax.servlet.http.Part

@Tag(name = "Project API")
interface ProjectService {
    fun findAbstractProjectEntityById(id: Long): AbstractProject

    fun findAbstractProjectByPublication(publication: AbstractPublication): AbstractProject?
    fun findAbstractProjectByAbstractFeedId(feedId: Long): AbstractProject?

    fun findAbstractProjectNameById(id: Long): String?

    fun listAbstractProjectsForAdmin(
        @Parameter(hidden = true) userId: Long,
        query: String,
        status: ProjectStatus,
        pageRequest: ExtendedPageRequest
    ): PageResponse<ProjectShortResponse>

    fun listProjectsForAdmin(
        @Parameter(hidden = true) userId: Long,
        query: String,
        status: ProjectStatus,
        pageRequest: ExtendedPageRequest
    ): PageResponse<ProjectShortResponse>

    fun listProjectsForUser(
        @Parameter(hidden = true) userId: Long,
        cursor: Cursor
    ): CursorResponse<ProjectShortResponse>

    fun listProjectsByUser(
        @Parameter(hidden = true) userId: Long,
        pageRequest: ExtendedPageRequest
    ): PageResponse<ProjectShortResponse>

    fun listProjectsForPublic(cursor: Cursor): CursorResponse<ProjectPublicShortResponse>

    fun findProjectById(
        @PathVariable projectId: Long,
        userId: Long,
        @Parameter(hidden = true) ignoreArchivedStatus: Boolean = false
    ): ProjectResponse

    fun previewProjectById(userId: Long, @PathVariable id: Long): ProjectPreviewResponse

    fun listEndowmentsForAdmin(
        @Parameter(hidden = true) userId: Long,
        query: String,
        status: ProjectStatus,
        pageRequest: ExtendedPageRequest
    ): PageResponse<ProjectShortResponse>

    fun listEndowmentsForUser(
        @Parameter(hidden = true) userId: Long,
        cursor: Cursor
    ): CursorResponse<ProjectShortResponse>

    fun listEndowmentsByUser(
        @Parameter(hidden = true) userId: Long,
        pageRequest: ExtendedPageRequest
    ): PageResponse<ProjectShortResponse>

    fun create(@RequestPart request: ProjectCreateRequest): ProjectResponse

    fun update(communityId: Long, name: String)

    fun update(@PathVariable id: Long, @RequestPart request: ProjectRequest): ProjectResponse

    fun archive(@PathVariable id: Long, @RequestPart request: ArchiveDto): ProjectResponse

    fun uploadPhoto(@PathVariable id: Long, @RequestPart file: Part): ProjectResponse

    fun delete(@PathVariable id: Long)

    fun listMembersForAdmin(
        @PathVariable id: Long,
        query: String,
        pageRequest: ExtendedPageRequest,
    ): PageResponse<MemberResponse>

    fun listMembersForUser(
        @PathVariable id: Long,
        query: String,
        pageRequest: ExtendedPageRequest,
    ): PageResponse<MemberResponse>

    fun participate(
        @Parameter(hidden = true) userId: Long,
        @PathVariable projectId: Long,
        @RequestBody request: ParticipationDto
    ): MemberResponse?
}
