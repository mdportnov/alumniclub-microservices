package ru.mephi.alumniclub.app.model.mapper.project

import org.springframework.data.domain.Page
import org.springframework.stereotype.Component
import ru.mephi.alumniclub.app.database.entity.project.AbstractProject
import ru.mephi.alumniclub.app.database.entity.project.Endowment
import ru.mephi.alumniclub.app.database.entity.project.Project
import ru.mephi.alumniclub.app.database.repository.community.CommunityDao
import ru.mephi.alumniclub.app.database.repository.community.UserCommunityDao
import ru.mephi.alumniclub.app.model.dto.CursorResponse
import ru.mephi.alumniclub.app.model.dto.PageResponse
import ru.mephi.alumniclub.app.model.dto.project.request.ProjectRequest
import ru.mephi.alumniclub.app.model.dto.project.response.ProjectPublicShortResponse
import ru.mephi.alumniclub.app.model.dto.project.response.ProjectResponse
import ru.mephi.alumniclub.app.model.dto.project.response.ProjectShortResponse
import ru.mephi.alumniclub.app.model.mapper.community.CommunityMapper
import ru.mephi.alumniclub.app.model.mapper.feed.FeedMapper
import ru.mephi.alumniclub.app.service.CommunityService

@Component
class ProjectMapper(
    private val feedMapper: FeedMapper,
    private val communityMapper: CommunityMapper,
    private val userCommunityDao: UserCommunityDao
) {
    fun asEndowment(request: ProjectRequest): Endowment {
        return Endowment(
            name = request.name,
            description = request.description,
            archive = false,
            color = request.color
        )
    }

    fun asProject(request: ProjectRequest): Project {
        return Project(
            name = request.name,
            description = request.description,
            archive = false,
            color = request.color
        )
    }

    fun update(project: AbstractProject, projectRequest: ProjectRequest): AbstractProject {
        project.name = projectRequest.name
        project.description = projectRequest.description
        project.color = projectRequest.color
        project.community.hidden = projectRequest.hiddenCommunity
        return project
    }

    fun asResponseForAdmin(project: AbstractProject): ProjectResponse {
        return ProjectResponse(
            id = project.id,
            createdAt = project.createdAt,
            type = project.getType(),
            name = project.name,
            description = project.description,
            archive = project.archive,
            joined = false,
            photoPath = project.photoPath,
            community = communityMapper.asResponse(project.community),
            publicationFeed = feedMapper.asResponse(project.publicationFeed),
            eventFeed = if (project is Project) feedMapper.asResponse(project.eventFeed) else null,
            color = project.color
        )
    }

    fun asResponseForUser(userId: Long, project: AbstractProject): ProjectResponse {
        return ProjectResponse(
            id = project.id,
            createdAt = project.createdAt,
            type = project.getType(),
            name = project.name,
            description = project.description,
            archive = project.archive,
            joined = userCommunityDao.existsByUserIdAndCommunity(userId, project.community),
            photoPath = project.photoPath,
            community = communityMapper.asResponse(project.community),
            publicationFeed = feedMapper.asResponse(project.publicationFeed),
            eventFeed = if (project is Project) feedMapper.asResponse(project.eventFeed) else null,
            color = project.color
        )
    }

    fun asPageResponse(userId: Long, projects: Page<AbstractProject>): PageResponse<ProjectShortResponse> {
        return PageResponse(
            content = projects.content.map { asShortResponse(userId, it) },
            number = projects.number.toLong(),
            numberOfElements = projects.numberOfElements.toLong(),
            totalPages = projects.totalPages.toLong()
        )
    }

    fun asCursorResponseForPublic(projects: Page<AbstractProject>): CursorResponse<ProjectPublicShortResponse> {
        return CursorResponse(
            content = projects.content.map { asShortResponseForPublic(it) },
            numberOfElements = projects.numberOfElements.toLong()
        )
    }

    fun asCursorResponseForUser(userId: Long, projects: Page<AbstractProject>): CursorResponse<ProjectShortResponse> {
        return CursorResponse(
            content = projects.content.map { asShortResponse(userId, it) },
            numberOfElements = projects.numberOfElements.toLong()
        )
    }

    private fun asShortResponse(userId: Long, project: AbstractProject): ProjectShortResponse {
        return ProjectShortResponse(
            id = project.id,
            createdAt = project.createdAt,
            type = project.getType(),
            name = project.name,
            membersCount = userCommunityDao.countByCommunityIdAndUserBannedFalse(project.community.id),
            joined = userCommunityDao.existsByUserIdAndCommunity(userId, project.community),
            archive = project.archive,
            photoPath = project.photoPath,
            color = project.color
        )
    }

    private fun asShortResponseForPublic(project: AbstractProject): ProjectPublicShortResponse {
        return ProjectPublicShortResponse(
            id = project.id,
            createdAt = project.createdAt,
            name = project.name,
            membersCount = userCommunityDao.countByCommunityIdAndUserBannedFalse(project.community.id),
            color = project.color
        )
    }
}
