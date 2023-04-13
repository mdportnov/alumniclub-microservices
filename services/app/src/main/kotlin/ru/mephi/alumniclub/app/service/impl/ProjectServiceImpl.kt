package ru.mephi.alumniclub.app.service.impl

import org.springframework.data.domain.Page
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.repository.Modifying
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import ru.mephi.alumniclub.app.database.entity.feed.AbstractFeed
import ru.mephi.alumniclub.app.database.entity.project.AbstractProject
import ru.mephi.alumniclub.app.database.entity.project.Project
import ru.mephi.alumniclub.app.database.entity.publication.AbstractPublication
import ru.mephi.alumniclub.app.database.entity.publication.Event
import ru.mephi.alumniclub.app.database.entity.publication.Publication
import ru.mephi.alumniclub.app.database.repository.project.AbstractProjectDao
import ru.mephi.alumniclub.app.database.repository.project.EndowmentDao
import ru.mephi.alumniclub.app.database.repository.project.ProjectDao
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
import ru.mephi.alumniclub.app.model.enumeration.NotificationCategory
import ru.mephi.alumniclub.app.model.enumeration.ProjectStatus
import ru.mephi.alumniclub.app.model.enumeration.ProjectType
import ru.mephi.alumniclub.app.model.exception.common.ResourceNotFoundException
import ru.mephi.alumniclub.app.model.mapper.project.ProjectMapper
import ru.mephi.alumniclub.app.service.*
import ru.mephi.alumniclub.shared.dto.ExtendedPageRequest
import ru.mephi.alumniclub.shared.dto.common.ApiError
import ru.mephi.alumniclub.shared.model.enums.StoreDir
import ru.mephi.alumniclub.shared.util.Cursor
import ru.mephi.alumniclub.shared.util.response.ResponseManager
import javax.servlet.http.Part
import javax.transaction.Transactional

@Component
@Transactional
class ProjectServiceImpl(
    // Storage Manager
    private val storageManager: StorageManager,

    // DAOs
    private val abstractProjectDao: AbstractProjectDao<AbstractProject>,
    private val projectDao: ProjectDao,
    private val endowmentDao: EndowmentDao,

    // Mapper
    private val mapper: ProjectMapper,

    // Services
    private val feedService: FeedService,
    private val publicationService: PublicationService,
    private val userService: UserService,
    private val communityService: CommunityService
) : ResponseManager(), ProjectService {
    /**
     * Returns a paginated list of [AbstractProject] whose name starts with [query] and
     * whose [status] corresponds to the [ProjectStatus].
     *
     * @param query The query string used to filter the list of projects by name.
     * @param status The [ProjectStatus] used to filter the list of projects by status.
     * @param pageRequest The [ExtendedPageRequest] used to paginate the list of projects.
     * @param dao The [AbstractProjectDao] used to retrieve the list of projects from the database.
     * @return A paginated list of [AbstractProject] that match the provided [query] and [status].
     */
    private fun <T : AbstractProject> listForAdmin(
        query: String, status: ProjectStatus, pageRequest: ExtendedPageRequest, dao: AbstractProjectDao<T>
    ): Page<AbstractProject> {
        return when (status) {
            ProjectStatus.ALL -> dao.findByNameStartsWith(query, pageRequest.pageable)
            ProjectStatus.ACTIVE -> dao.findByNameStartsWithAndArchive(query, false, pageRequest.pageable)
            ProjectStatus.ARCHIVED -> dao.findByNameStartsWithAndArchive(query, true, pageRequest.pageable)
        }.map { it as AbstractProject }
    }

    /**
     * Returns a paginated list of [AbstractProject] that are not archived and were either created before or after
     * the cursor's `from` time, depending on the cursor's `Chronology`.
     *
     * @param cursor The [Cursor] object containing chronology and pagination data
     * @param dao The [AbstractProjectDao] used to retrieve the list of projects from the database.
     * @return A paginated list of [AbstractProject] that match the provided filters.
     */
    private fun <T : AbstractProject> listForUser(cursor: Cursor, dao: AbstractProjectDao<T>): Page<AbstractProject> {
        return when (cursor.chronology) {
            Cursor.Chronology.BEFORE -> dao.findByArchiveFalseAndCreatedAtBefore(cursor.from, cursor.page)
            Cursor.Chronology.AFTER -> dao.findByArchiveFalseAndCreatedAtAfter(cursor.from, cursor.page)
        }.map { it as AbstractProject }
    }

    /**
     * Returns a paginated list of [AbstractProject] that are not archived and that the user is a member of.
     *
     * @param userId The user's id used to filter the list of projects by the user's membership.
     * @param pageRequest The [ExtendedPageRequest] used to paginate the list of projects.
     * @param dao The [AbstractProjectDao] used to retrieve the list of projects from the database.
     * @return A paginated list of [AbstractProject] that match the provided filters.
     */
    private fun <T : AbstractProject> listByUser(
        userId: Long, pageRequest: ExtendedPageRequest, dao: AbstractProjectDao<T>
    ): Page<AbstractProject> {
        val user = userService.findUserEntityById(userId)
        return dao.findByCommunityUsersContainingAndArchiveFalse(user, pageRequest.pageable)
            .map { it as AbstractProject }
    }

    /**
     * Creates a new project from a given [ProjectRequest] and returns a [ProjectResponse] containing the
     * created [Project]'s information.
     *
     * @param request The [ProjectRequest] containing the new project's information.
     * @throws ApiError when a project with the same name already exists.
     * @return A [ProjectResponse] containing the created project's information.
     */
    private fun createProject(request: ProjectRequest): ProjectResponse {
        if (projectDao.existsByName(request.name))
            throw ApiError(HttpStatus.CONFLICT, i18n("exception.alreadyExists.project", request.name))
        val project = mapper.asProject(request).apply {
            publicationFeed = feedService.createPublicationFeedEntity(request.name, NotificationCategory.PROJECTS)
            eventFeed = feedService.createEventFeedEntity(request.name)
            community = communityService.create(this)
            projectDao.save(this)
            community.hidden = request.hiddenCommunity
            community.project = this
        }
        return mapper.asResponseForAdmin(project)
    }

    /**
     * Creates a new project from a given [ProjectRequest] and returns a [ProjectResponse] containing the
     * created [Project]'s information.
     *
     * @param request The [ProjectRequest] containing the new endowment's information.
     * @throws ApiError when an endowment with the same name already exists.
     * @return A [ProjectResponse] containing the created endowment's information.
     */
    private fun createEndowment(request: ProjectRequest): ProjectResponse {
        if (endowmentDao.existsByName(request.name))
            throw ApiError(HttpStatus.CONFLICT, i18n("exception.alreadyExists.endowment", request.name))
        val endowment = mapper.asEndowment(request).apply {
            publicationFeed = feedService.createPublicationFeedEntity(request.name, NotificationCategory.ENDOWMENTS)
            community = communityService.create(this)
            endowmentDao.save(this)
            community.hidden = request.hiddenCommunity
            community.project = this
        }
        return mapper.asResponseForAdmin(endowment)
    }

    /**
     * Update the name of the publication feed and event feed associated with the provided [project].
     *
     * @param project The [AbstractProject] whose feeds should be updated
     * @param request The [ProjectRequest] containing the new feed name
     */
    @Modifying
    private fun updateFeeds(project: AbstractProject, request: ProjectRequest) {
        feedService.update(project.publicationFeed, request.name)
        if (project is Project) feedService.update(project.eventFeed, request.name)
    }

    /**
     * Retrieve an [AbstractProject] by [id], throws [ApiError] if not found or the project is archived.
     *
     * @param id The unique identifier of the [AbstractProject] to be retrieved.
     * @return An [AbstractProject] if it exists and is not archived.
     * @throws ApiError if the project is not found or is archived.
     */
    private fun findAbstractProjectEntityByIdAndArchiveFalse(id: Long): AbstractProject {
        return findAbstractProjectEntityById(id).takeIf { !it.archive } ?:
            throw ApiError(HttpStatus.FORBIDDEN, "exception.forbidden.projectArchived")
    }

    /**
     * Retrieves an [AbstractProject] by its ID
     *
     * @param id The ID of the [AbstractProject] to be retrieved
     * @return The [AbstractProject] object that has the provided ID
     * @throws ResourceNotFoundException if the [AbstractProject] with the provided ID does not exist in the database
     */
    override fun findAbstractProjectEntityById(id: Long): AbstractProject {
        return abstractProjectDao.findById(id)
            .orElseThrow { ResourceNotFoundException(AbstractProject::class.java, id) }
    }

    /**
     * Lists all [AbstractProject]s for an admin user, based on the provided search query, status
     * and pageRequest.
     *
     * @param userId The id of the admin user making the request
     * @param query A string used to search for projects by name
     * @param status A [ProjectStatus] enum used to determine the type of search to perform (ALL, ACTIVE, or ARCHIVED)
     * @param pageRequest An [ExtendedPageRequest] object used for pagination
     * @return A [PageResponse] of [ProjectShortResponse] objects, containing the list of projects
     * and pagination information
     */
    override fun listAbstractProjectsForAdmin(
        userId: Long, query: String, status: ProjectStatus, pageRequest: ExtendedPageRequest
    ): PageResponse<ProjectShortResponse> {
        val projects = listForAdmin(query, status, pageRequest, abstractProjectDao)
        return mapper.asPageResponse(userId, projects)
    }

    /**
     * Lists all [Project]s for an admin user, based on the provided search query, status, and pageRequest.
     *
     * @param userId The id of the admin user making the request
     * @param query A string used to search for projects by name
     * @param status A [ProjectStatus] enum used to determine the type of search to perform (ALL, ACTIVE, or ARCHIVED)
     * @param pageRequest An [ExtendedPageRequest] object used for pagination
     * @return A [PageResponse] of [ProjectShortResponse] objects, containing the list of projects
     * and pagination information
     */
    override fun listProjectsForAdmin(
        userId: Long, query: String, status: ProjectStatus, pageRequest: ExtendedPageRequest
    ): PageResponse<ProjectShortResponse> {
        val projects = listForAdmin(query, status, pageRequest, projectDao)
        return mapper.asPageResponse(userId, projects)
    }

    /**
     * Lists all [Project]s for an admin user, based on the provided search query, status, and pageRequest.
     *
     * @param userId The id of the admin user making the request
     * @param query A string used to search for endowments by name
     * @param status A [ProjectStatus] enum used to determine the type of search to perform (ALL, ACTIVE, or ARCHIVED)
     * @param pageRequest An [ExtendedPageRequest] object used for pagination
     * @return A [PageResponse] of [ProjectShortResponse] objects, containing the list of
     * endowments and pagination information
     */
    override fun listEndowmentsForAdmin(
        userId: Long, query: String, status: ProjectStatus, pageRequest: ExtendedPageRequest
    ): PageResponse<ProjectShortResponse> {
        val endowments = listForAdmin(query, status, pageRequest, endowmentDao)
        return mapper.asPageResponse(userId, endowments)
    }

    /**
     * Returns a [CursorResponse] of [ProjectShortResponse] objects for the user with the provided ID.
     *
     * @param userId The ID of the user for whom to list projects.
     * @param cursor A [Cursor] object used for pagination.
     * @return A [CursorResponse] of [ProjectShortResponse] objects.
     */
    override fun listProjectsForUser(userId: Long, cursor: Cursor): CursorResponse<ProjectShortResponse> {
        val projects = listForUser(cursor, projectDao)
        return mapper.asCursorResponseForUser(userId, projects)
    }

    /**
     * Returns a [CursorResponse] of [ProjectPublicShortResponse] objects for the public.
     *
     * @param cursor A [Cursor] object used for pagination.
     * @return A [CursorResponse] of [ProjectPublicShortResponse] objects.
     */
    override fun listProjectsForPublic(cursor: Cursor): CursorResponse<ProjectPublicShortResponse> {
        val projects = listForUser(cursor, projectDao)
        return mapper.asCursorResponseForPublic(projects)
    }

    /**
     * Returns a [CursorResponse] response of [ProjectShortResponse] objects for the user with the provided ID.
     *
     * @param userId The ID of the user for whom to list endowments.
     * @param cursor A [Cursor] object used for pagination.
     * @return A [CursorResponse] of [ProjectShortResponse] objects.
     */
    override fun listEndowmentsForUser(userId: Long, cursor: Cursor): CursorResponse<ProjectShortResponse> {
        val endowments = listForUser(cursor, endowmentDao)
        return mapper.asCursorResponseForUser(userId, endowments)
    }

    /**
     * Returns a [PageResponse] of [ProjectShortResponse] of all projects that are joined by the given user.
     *
     * @param userId The ID of the user
     * @param pageRequest An [ExtendedPageRequest] object used for pagination.
     * @return a [PageResponse] of [ProjectShortResponse] of all projects that are joined by the given user.
     */
    override fun listProjectsByUser(
        userId: Long, pageRequest: ExtendedPageRequest
    ): PageResponse<ProjectShortResponse> {
        val projects = listByUser(userId, pageRequest, projectDao)
        return mapper.asPageResponse(userId, projects)
    }

    /**
     * Returns a [PageResponse] of [ProjectShortResponse] of all endowments that are joined by the given user.
     *
     * @param userId The ID of the user
     * @param pageRequest An [ExtendedPageRequest] object used for pagination.
     * @return a [PageResponse] of [ProjectShortResponse] of all endowments that are joined by the given user.
     */
    override fun listEndowmentsByUser(
        userId: Long, pageRequest: ExtendedPageRequest
    ): PageResponse<ProjectShortResponse> {
        val endowments = listByUser(userId, pageRequest, endowmentDao)
        return mapper.asPageResponse(userId, endowments)
    }

    /**
     * Returns the [AbstractProject] that is associated with the given feed ID.
     *
     * @param feedId The [AbstractFeed] ID associated with the project.
     * @return the [AbstractProject].
     */
    override fun findAbstractProjectByAbstractFeedId(feedId: Long): AbstractProject? {
        return abstractProjectDao.findByPublicationFeedId(feedId) ?: projectDao.findByEventFeedId(feedId)
    }

    /**
     * Returns the [AbstractProject] name is associated with the given id.
     *
     * @param id The id associated with the project.
     * @return The name of the project
     */
    override fun findAbstractProjectNameById(id: Long): String? {
        return abstractProjectDao.findNameById(id)
    }

    /**
     * Find a [Project] by its ID and returns a [ProjectResponse]
     *
     * @param projectId the ID of the project to find
     * @param userId the ID of the user making the request
     * @param ignoreArchivedStatus a flag indicating whether to ignore the archive status of the project
     * @return a [ProjectResponse] object representing the found project
     * @throws ApiError with status code [HttpStatus.FORBIDDEN] if the project is archived and
     * `ignoreArchive` is `false`
     */
    override fun findProjectById(projectId: Long, userId: Long, ignoreArchivedStatus: Boolean): ProjectResponse {
        val project = findAbstractProjectEntityById(projectId)
        if (!ignoreArchivedStatus && project.archive)
            throw ApiError(HttpStatus.FORBIDDEN, "exception.forbidden.projectArchived")
        return mapper.asResponseForUser(userId, project)
    }

    /**
     * Find a [Project] by its ID and returns a [ProjectPreviewResponse]
     *
     * @param userId the ID of the user making the request
     * @param id the ID of the project to find
     * @return a [ProjectPreviewResponse] object representing the found project
     */
    override fun previewProjectById(userId: Long, id: Long): ProjectPreviewResponse {
        val project = findAbstractProjectEntityByIdAndArchiveFalse(id)
        val pageRequest = ExtendedPageRequest(page = 1, size = 3, order = Sort.Direction.DESC, field = "createdAt")
        val publications = publicationService.listPublicationsByFeedIdForUser(
            userId,
            project.publicationFeed.id,
            pageRequest = pageRequest
        ).content
        val events = if (project is Project) publicationService.listEventsByFeedIdForUser(
            userId,
            project.eventFeed.id,
            pageRequest = pageRequest
        ).content else emptyList()
        return ProjectPreviewResponse(publications = publications, events = events)
    }

    /**
     * Create a new [Project] and returns a [ProjectResponse]
     *
     * @param request the [ProjectCreateRequest] containing the information to create a new project
     * @return a [ProjectResponse] object representing the created project
     */
    override fun create(request: ProjectCreateRequest): ProjectResponse {
        return when (request.type) {
            ProjectType.PROJECT -> createProject(request)
            ProjectType.ENDOWMENT -> createEndowment(request)
        }
    }

    /**
     * Updates the [AbstractProject] name with the given communityId.
     *
     * @param communityId ID of the community
     * @param name New name of the project
     * @throws ResourceNotFoundException if the project with given [communityId] doesn't exist
     */
    @Modifying
    override fun update(communityId: Long, name: String) {
        val project = abstractProjectDao.findByCommunityId(communityId) ?: throw ResourceNotFoundException(
            AbstractProject::class.java,
            "communityId"
        )
        project.name = name
    }

    /**
     * Updates the project with the given ID and [ProjectRequest].
     *
     * @param id ID of the project to update
     * @param request [ProjectRequest] payload for updating the project
     * @return Updated project response
     * @throws ResourceNotFoundException if the project with given ID doesn't exist
     * @throws ApiError if another project with the same name already exists
     */
    @Modifying
    override fun update(id: Long, request: ProjectRequest): ProjectResponse {
        if (projectDao.existsByNameAndIdNot(request.name, id))
            throw ApiError(HttpStatus.CONFLICT, i18n("exception.alreadyExists.project", request.name))
        val existingProject = findAbstractProjectEntityById(id)
        val project = mapper.update(existingProject, request)
        updateFeeds(project, request)
        communityService.update(project.community, request.name, request.hiddenCommunity)
        return mapper.asResponseForAdmin(project)
    }

    /**
     * Archives the project with the given ID.
     *
     * @param id ID of the project to archive
     * @param request [ArchiveDto] payload for archiving the project
     * @return Archived project response
     * @throws ResourceNotFoundException if the project with given id doesn't exist
     */
    @Modifying
    override fun archive(id: Long, request: ArchiveDto): ProjectResponse {
        val project = findAbstractProjectEntityById(id).apply { archive = request.archive }
        if (request.archive) communityService.hide(project.community)
        else communityService.show(project.community)
        if (project is Project) publicationService.setHiddenToAllEventsInFeed(project.eventFeed.id, request.archive)
        publicationService.setHiddenToAllPublicationsInFeed(project.publicationFeed.id, request.archive)
        return mapper.asResponseForAdmin(project)
    }

    /**
     * Updates the photo for a project with the given ID.
     *
     * @param id The ID of the [Project] to update the photo for
     * @param file The photo to upload
     * @return the updated project
     * @throws ResourceNotFoundException if the project with id is not found
     */
    @Modifying
    override fun uploadPhoto(id: Long, file: Part): ProjectResponse {
        val project = findAbstractProjectEntityById(id)
        project.photoPath = storageManager.saveImage(file, StoreDir.PROJECT, project.photoPath, true)
        return mapper.asResponseForAdmin(project)
    }

    /**
     * Deletes the project with the given ID.
     *
     * @param id The id of the project to delete
     * @throws ResourceNotFoundException if the project with ID is not found
     */
    @Modifying
    override fun delete(id: Long) {
        val project = findAbstractProjectEntityById(id)
        abstractProjectDao.delete(project)
        storageManager.removeImage(project.photoPath, StoreDir.PROJECT)
    }

    /**
     * Lists all members of a [Project].
     *
     * @param id The ID of the project to list members for
     * @param query The query to search for members
     * @param pageRequest The page request for the list of members
     * @return a page response of members for the project
     * @throws ResourceNotFoundException if the project with ID is not found
     */
    override fun listMembersForAdmin(
        id: Long, query: String, pageRequest: ExtendedPageRequest
    ): PageResponse<MemberResponse> {
        val project = findAbstractProjectEntityById(id)
        return communityService.listMembersForAdmin(project.community.id, query, pageRequest)
    }

    /**
     * Returns the short representation of all members for a specific project with ID and filters the result with query.
     * The result is returned as a paginated response.
     *
     * @param id The id of the project
     * @param query Filter for the members to be returned
     * @param pageRequest Pagination information for the members to be returned
     * @return [PageResponse] with a list of [MemberResponse]
     * @throws [ApiError] with status code FORBIDDEN and message
     * "exception.project.archived" if the project is archived.
     */
    override fun listMembersForUser(
        id: Long, query: String, pageRequest: ExtendedPageRequest
    ): PageResponse<MemberResponse> {
        val project = findAbstractProjectEntityById(id)
        if (project.archive) throw ApiError(HttpStatus.FORBIDDEN, "exception.forbidden.projectArchived")
        return communityService.listMembersForUser(project.community.id, query, pageRequest)
    }

    /**
     * Allows a user with userId to participate in the project with projectId using the provided request.
     *
     * @param userId The id of the user
     * @param projectId The id of the project
     * @param request The request containing information about the participation
     * @return [MemberResponse] representing the short information of the user participating in the project
     */
    override fun participate(userId: Long, projectId: Long, request: ParticipationDto): MemberResponse? {
        val project = findAbstractProjectEntityById(projectId)
        return communityService.participate(userId, project.community.id, request, allowProject = true)
    }

    /**
     * Returns the [AbstractProject] associated to the provided publication.
     *
     * @param publication The abstract publication for which the abstract project is to be found
     * @return [AbstractProject] associated to the provided abstract publication
     */
    override fun findAbstractProjectByPublication(publication: AbstractPublication): AbstractProject? {
        return when (publication) {
            is Publication -> abstractProjectDao.findByPublicationFeed(publication.feed)
            is Event -> projectDao.findByEventFeed(publication.feed)
            else -> null
        }
    }
}
