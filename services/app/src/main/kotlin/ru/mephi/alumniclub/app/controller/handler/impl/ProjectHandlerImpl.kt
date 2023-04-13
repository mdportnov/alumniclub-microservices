package ru.mephi.alumniclub.app.controller.handler.impl

import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse
import org.springframework.web.servlet.function.ServerResponse.ok
import org.springframework.web.servlet.function.body
import ru.mephi.alumniclub.app.controller.handler.ProjectHandler
import ru.mephi.alumniclub.app.model.dto.ParticipationDto
import ru.mephi.alumniclub.app.model.dto.project.ArchiveDto
import ru.mephi.alumniclub.app.model.dto.project.request.ProjectCreateRequest
import ru.mephi.alumniclub.app.model.dto.project.request.ProjectRequest
import ru.mephi.alumniclub.app.model.enumeration.ProjectStatus
import ru.mephi.alumniclub.app.model.enumeration.ProjectType
import ru.mephi.alumniclub.app.service.ProjectService
import ru.mephi.alumniclub.shared.dto.common.ApiError
import ru.mephi.alumniclub.shared.dto.common.ApiMessage
import ru.mephi.alumniclub.shared.dto.security.ScopePermission
import ru.mephi.alumniclub.shared.util.extension.*
import ru.mephi.alumniclub.shared.util.response.ResponseManager

@Component
class ProjectHandlerImpl(
    private val service: ProjectService
) : ResponseManager(), ProjectHandler {
    private fun assertHasProjectPermission(request: ServerRequest, projectId: Long) {
        if (request.hasOneOfPermission(ScopePermission.PROJECTS_CONTROL)) return
        val projects = request.getAuthenticationToken().getPermissions().projects
        if (projectId in projects) return
        throw ApiError(HttpStatus.FORBIDDEN, "exception.forbidden.resource")
    }

    override fun listForAdmin(request: ServerRequest): ServerResponse {
        request.assertHasOneOfPermission(ScopePermission.PROJECTS_VIEW)
        val userId = request.getPrincipal()
        val query = request.paramOrElse("query") { "" }.trim()
        val page = request.getExtendedPageRequest()
        val status = request.paramOrElse("status") { ProjectStatus.ALL }
        val projects = when (request.paramOrElse<ProjectType?>("type") { null }) {
            ProjectType.PROJECT -> service.listProjectsForAdmin(userId, query, status, page)
            ProjectType.ENDOWMENT -> service.listEndowmentsForAdmin(userId, query, status, page)
            else -> service.listAbstractProjectsForAdmin(userId, query, status, page)
        }
        return projects.toOkBody()
    }

    override fun listProjectsForPublic(request: ServerRequest): ServerResponse {
        val cursor = request.getCursorRequest()
        val projects = service.listProjectsForPublic(cursor)
        return projects.toOkBody()
    }

    override fun listForUser(request: ServerRequest): ServerResponse {
        val userId = request.getPrincipal()
        val cursor = request.getCursorRequest()
        val projects = when (request.paramOrElse("type") { ProjectType.PROJECT }) {
            ProjectType.PROJECT -> service.listProjectsForUser(userId, cursor)
            ProjectType.ENDOWMENT -> service.listEndowmentsForUser(userId, cursor)
        }
        return projects.toOkBody()
    }

    override fun listByUser(request: ServerRequest): ServerResponse {
        val userId = request.getPrincipal()
        val page = request.getExtendedPageRequest()
        val projects = when (request.paramOrElse("type") { ProjectType.PROJECT }) {
            ProjectType.PROJECT -> service.listProjectsByUser(userId, page)
            ProjectType.ENDOWMENT -> service.listEndowmentsByUser(userId, page)
        }
        return projects.toOkBody()
    }

    override fun findProjectByIdForUser(request: ServerRequest): ServerResponse {
        val userId = request.getPrincipal()
        val projectId = request.pathVariableOrThrow<Long>("id")
        val project = service.findProjectById(projectId, userId)
        return project.toOkBody()
    }

    override fun findProjectByIdForAdmin(request: ServerRequest): ServerResponse {
        request.assertHasOneOfPermission(ScopePermission.PROJECTS_VIEW)
        val userId = request.getPrincipal()
        val projectId = request.pathVariableOrThrow<Long>("id")
        val project = service.findProjectById(projectId, userId, ignoreArchivedStatus = true)
        return project.toOkBody()
    }

    override fun previewProjectById(request: ServerRequest): ServerResponse {
        val id = request.pathVariableOrThrow<Long>("id")
        val preview = service.previewProjectById(request.getPrincipal(), id)
        return preview.toOkBody()
    }

    override fun create(request: ServerRequest): ServerResponse {
        request.assertHasOneOfPermission(ScopePermission.PROJECTS_CONTROL)
        val projectRequest = request.body<ProjectCreateRequest>()
        validate(projectRequest)
        val project = service.create(projectRequest)
//        permissionService.assignProjectPermission(request.getPrincipal(), project.id)
        return ApiMessage(i18n("label.common.created"), project)
            .toCreatedResponse("/api/v1/project/${project.id}")
    }

    override fun update(request: ServerRequest): ServerResponse {
        val projectId = request.pathVariableOrThrow<Long>("id")
        assertHasProjectPermission(request, projectId)
        val projectRequest = request.body<ProjectRequest>()
        validate(projectRequest)
        val project = service.update(projectId, projectRequest)
        return ApiMessage(message = i18n("label.common.updated"), project).toOkBody()
    }

    override fun uploadPhoto(request: ServerRequest): ServerResponse {
        val projectId = request.pathVariableOrThrow<Long>("id")
        assertHasProjectPermission(request, projectId)
        val file = request.getMultiPartPhoto()
            ?: throw ApiError(HttpStatus.BAD_REQUEST, i18n("exception.media.invalidMultipart"))
        val project = service.uploadPhoto(projectId, file)
        return ApiMessage(message = i18n("label.common.updated"), project).toOkBody()
    }

    override fun archive(request: ServerRequest): ServerResponse {
        val projectId = request.pathVariableOrThrow<Long>("id")
        assertHasProjectPermission(request, projectId)
        val archiveDto = request.body<ArchiveDto>()
        val project = service.archive(projectId, archiveDto)
        return ApiMessage(message = i18n("label.common.updated"), project).toOkBody()
    }

    override fun delete(request: ServerRequest): ServerResponse {
        val projectId = request.pathVariableOrThrow<Long>("id")
        assertHasProjectPermission(request, projectId)
        service.delete(projectId)
        return ApiMessage(data = null, message = i18n("label.common.deleted")).toOkBody()
    }

    override fun listMembersForUser(request: ServerRequest): ServerResponse {
        val id = request.pathVariableOrThrow<Long>("id")
        val query = request.paramOrElse("query") { "" }.trim()
        val page = request.getExtendedPageRequest()
        val participants = service.listMembersForUser(id, query, page)
        return ok().body(participants)
    }

    override fun listMembersForAdmin(request: ServerRequest): ServerResponse {
        request.assertHasOneOfPermission(ScopePermission.PROJECTS_VIEW)
        val id = request.pathVariableOrThrow<Long>("id")
        val page = request.getExtendedPageRequest()
        val query = request.paramOrElse("query") { "" }.trim()
        val participants = service.listMembersForAdmin(id, query, page)
        return ok().body(participants)
    }

    override fun participate(request: ServerRequest): ServerResponse {
        val participationDto = request.body<ParticipationDto>()
        val response = service.participate(
            userId = request.getPrincipal(),
            projectId = request.pathVariableOrThrow("id"),
            request = participationDto
        )
        val message = if (participationDto.participation) {
            i18n("label.project.join")
        } else i18n("label.project.leave")
        return ApiMessage(data = response, message = message).toOkBody()
    }
}
