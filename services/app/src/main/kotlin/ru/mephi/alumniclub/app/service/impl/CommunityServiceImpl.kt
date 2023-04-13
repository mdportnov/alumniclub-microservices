package ru.mephi.alumniclub.app.service.impl

import org.springframework.context.annotation.Lazy
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.jpa.repository.Modifying
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import ru.mephi.alumniclub.app.database.entity.community.Community
import ru.mephi.alumniclub.app.database.entity.community.UserCommunity
import ru.mephi.alumniclub.app.database.entity.project.AbstractProject
import ru.mephi.alumniclub.app.database.entity.user.User
import ru.mephi.alumniclub.app.database.repository.community.CommunityDao
import ru.mephi.alumniclub.app.database.repository.community.UserCommunityDao
import ru.mephi.alumniclub.app.model.dto.CursorResponse
import ru.mephi.alumniclub.app.model.dto.PageResponse
import ru.mephi.alumniclub.app.model.dto.ParticipationDto
import ru.mephi.alumniclub.app.model.dto.community.request.CommunityRequest
import ru.mephi.alumniclub.app.model.dto.community.response.CommunityResponse
import ru.mephi.alumniclub.app.model.dto.community.response.MemberResponse
import ru.mephi.alumniclub.app.model.enumeration.community.roleCommunities
import ru.mephi.alumniclub.app.model.enumeration.user.Role
import ru.mephi.alumniclub.app.model.exception.common.ResourceNotFoundException
import ru.mephi.alumniclub.app.model.mapper.community.CommunityMapper
import ru.mephi.alumniclub.app.model.mapper.community.UserCommunityMapper
import ru.mephi.alumniclub.app.service.CommunityService
import ru.mephi.alumniclub.app.service.ProjectService
import ru.mephi.alumniclub.app.service.StorageManager
import ru.mephi.alumniclub.app.service.UserService
import ru.mephi.alumniclub.shared.dto.ExtendedPageRequest
import ru.mephi.alumniclub.shared.dto.common.ApiError
import ru.mephi.alumniclub.shared.model.enums.StoreDir
import ru.mephi.alumniclub.shared.util.Cursor
import ru.mephi.alumniclub.shared.util.response.ResponseManager
import javax.servlet.http.Part
import javax.transaction.Transactional

@Service
class CommunityServiceImpl(
    private var storageManager: StorageManager,
    private val communityDao: CommunityDao,
    private val userCommunityDao: UserCommunityDao,
    private var communityMapper: CommunityMapper,
    private val userCommunityMapper: UserCommunityMapper,
    @Lazy
    private val projectService: ProjectService,
    private val userService: UserService,
) : ResponseManager(), CommunityService {
    /**
     * Retrieves a [Community] entity by its ID and that is not hidden.
     *
     * @param id The ID of the community to retrieve.
     * @return The [Community] entity that matches the provided ID.
     * @throws ApiError with a [HttpStatus.FORBIDDEN] status code if the community is hidden.
     */
    private fun findEntityByIdAndHiddenFalse(id: Long): Community {
        val community = findEntityById(id)
        if (community.hidden) throw ApiError(HttpStatus.FORBIDDEN, i18n("exception.forbidden.resource"))
        return community
    }

    /**
     * Returns the [Community] object with the given [id].
     *
     * @param id The ID of the community to retrieve.
     * @throws ApiError with [HttpStatus.CONFLICT] if the community is a role community.
     * @return The [Community] object with the given [id].
     */
    private fun listMembers(
        id: Long, query: String, page: PageRequest,
        showHiddenCommunities: Boolean = false,
        showBannedUsers: Boolean = false
    ): Page<UserCommunity> {
        val community = if (showHiddenCommunities) findEntityById(id) else findEntityByIdAndHiddenFalse(id)
        return with(userCommunityDao) {
            if (showBannedUsers) findByCommunityAndUserSurnameStartsWith(community, query, page)
            else findByCommunityAndUserSurnameStartsWithAndUserBannedFalse(community, query, page)
        }
    }

    /**
     * Joins the community specified by [communityId] with the user specified by [userId].
     *
     * @param communityId The ID of the community to join.
     * @param userId The ID of the user joining the community.
     * @throws ApiError with [HttpStatus.FORBIDDEN] if the specified community is hidden.
     * @throws ApiError with [HttpStatus.CONFLICT] if the specified user is already a member of the specified community.
     * @return A [MemberShortResponse] representing the joined community.
     */
    @Modifying
    @Transactional
    fun join(communityId: Long, userId: Long): MemberResponse {
        val community = findEntityByIdAndHiddenFalse(communityId)
        val user = userService.findUserEntityById(userId)
        if (userCommunityDao.existsByUserAndCommunity(user, community))
            throw ApiError(HttpStatus.CONFLICT, i18n("exception.alreadyExists.communityMember"))
        val userCommunity = UserCommunity(user = user, community = community).also { userCommunityDao.save(it) }
        return userCommunityMapper.asResponse(userCommunity)
    }

    /**
     * Leaves the community specified by [communityId] for the user specified by [userId].
     *
     * @param communityId The ID of the community to leave.
     * @param userId The ID of the user leaving the community.
     * @throws ApiError with [HttpStatus.CONFLICT] if the specified user is not a member of the specified community.
     */
    @Modifying
    @Transactional
    fun leave(communityId: Long, userId: Long) {
        val community = findEntityById(communityId)
        val user = userService.findUserEntityById(userId)
        val userCommunity = userCommunityDao.findByUserAndCommunity(user, community)
            .orElseThrow { ApiError(HttpStatus.CONFLICT, i18n("exception.notFound.communityMember")) }
        userCommunityDao.delete(userCommunity)
    }

    /**
     * Returns a [Community] entity with the given [id].
     *
     * @param id The ID of the [Community] entity to retrieve.
     * @return The [Community] entity with the given [id].
     * @throws ResourceNotFoundException if the [Community] entity does not exist.
     */
    override fun findEntityById(id: Long): Community {
        return communityDao.findById(id).orElseThrow { ResourceNotFoundException(Community::class.java, id) }
    }

    /**
     * Returns a paginated list of [CommunityResponse] entities filtered by [query].
     *
     * @param query The query string used to filter the list of [CommunityResponse] entities.
     * @param pageRequest The [PageRequest] used to paginate the list of [CommunityResponse] entities.
     * @return A paginated list of [CommunityResponse] entities that match the provided [query].
     */
    override fun list(query: String, pageRequest: ExtendedPageRequest): PageResponse<CommunityResponse> {
        val communities = communityDao.findByNameStartsWith(query, pageRequest.pageable)
        return communityMapper.asPageResponse(communities)
    }

    /**
     * Retrieves a paginated list of communities associated with a user.
     *
     * @param userId The ID of the user to retrieve the communities for.
     * @param pageRequest The [PageRequest] used to paginate the list of communities.
     * @param includeHidden A flag indicating whether to include hidden communities in the result.
     * @return A [PageResponse] containing the paginated list of [CommunityResponse] matching the provided criteria.
     */
    override fun listByUser(userId: Long, pageRequest: ExtendedPageRequest, includeHidden: Boolean): PageResponse<CommunityResponse> {
        val communities = with(userCommunityDao) {
            if (includeHidden) findByUserId(userId, pageRequest.pageable)
            else findByUserIdAndCommunityHiddenFalse(userId, pageRequest.pageable)
        }.map { it.community }
        return communityMapper.asPageResponse(communities)
    }

    /**
     * Returns a paginated list of [CommunityResponse] which the user is not a member of filtered by chronology.
     *
     * @param userId The ID of the user used to filter the list of communities.
     * @param cursor The [Cursor] used to filter and paginate the list of communities.
     * @return A paginated list of [CommunityResponse] which the user is not a member of.
     */
    override fun listByUserNot(userId: Long, cursor: Cursor): CursorResponse<CommunityResponse> {
        val user = userService.findUserEntityById(userId)
        val communities = with(communityDao) {
            when (cursor.chronology) {
                Cursor.Chronology.BEFORE ->
                    findByUsersNotContainingAndHiddenFalseAndCreatedAtBefore(user, cursor.from, cursor.page)

                Cursor.Chronology.AFTER ->
                    findByUsersNotContainingAndHiddenFalseAndCreatedAtAfter(user, cursor.from, cursor.page)
            }
        }
        return communityMapper.asCursorResponse(communities)
    }

    /**
     * Finds a community by its ID.
     *
     * @param id The ID of the community to find.
     * @param includeHidden Determines whether to return hidden communities or not.
     * @return A [CommunityResponse] that matches the provided ID.
     */
    override fun findById(id: Long, includeHidden: Boolean): CommunityResponse {
        val community = if (includeHidden) findEntityById(id) else findEntityByIdAndHiddenFalse(id)
        return communityMapper.asResponse(community)
    }

    /**
     * Creates a new community with a given name.
     *
     * @param project The [AbstractProject] used to create the community.
     * @return The newly created [Community].
     * @throws ApiError if the community name already exists.
     */
    override fun create(project: AbstractProject): Community {
        if (communityDao.existsByName(project.name))
            throw ApiError(HttpStatus.CONFLICT, i18n("exception.alreadyExists.community", project.name))
        return communityMapper.asEntity(project.name)
    }

    /**
     * Creates a new community and returns its representation as a [CommunityResponse].
     *
     * @param request The [CommunityRequest] containing the information for the community to be created.
     * @return The [CommunityResponse] representation of the newly created community.
     * @throws ApiError if a community with the same name already exists.
     */
    override fun create(request: CommunityRequest): CommunityResponse {
        if (communityDao.existsByName(request.name))
            throw ApiError(HttpStatus.CONFLICT, i18n("exception.alreadyExists.community", request.name))
        val community = Community(request.name).also { communityDao.save(it) }
        return communityMapper.asResponse(community)
    }

    /**
     * Updates the given [Community] with the provided [name] and [hidden] values.
     *
     * @param community The [Community] to be updated.
     * @param name The new name for the community.
     * @param hidden The new hidden value for the community.
     * @return The updated [Community].
     */
    @Modifying
    @Transactional
    override fun update(community: Community, name: String, hidden: Boolean): Community {
        val request = CommunityRequest(name = name, hidden = hidden)
        return communityMapper.update(community, request)
    }

    /**
     * Updates the community with the provided [id] with the information in the [request].
     *
     * @param id The ID of the community to be updated.
     * @param request The [CommunityRequest] containing the information to update the community with.
     * @return The [CommunityResponse] representation of the updated community.
     * @throws ApiError if a community with the same name already exists.
     * @throws ApiError if the community is a role community.
     */
    @Modifying
    @Transactional
    override fun update(id: Long, request: CommunityRequest): CommunityResponse {
        if (communityDao.existsByNameAndIdNot(request.name, id))
            throw ApiError(HttpStatus.CONFLICT, i18n("exception.alreadyExists.community", request.name))
        val community = findEntityById(id)
        if (community.role) throw ApiError(HttpStatus.CONFLICT, i18n("exception.community.roleAction"))
        communityMapper.update(community, request)
        if (community.project != null) projectService.update(id, request.name)
        return communityMapper.asResponse(community)
    }

    /**
     * Sets the hidden status to true of a [Community].
     *
     * @param community The [Community] to be updated.
     * @throws ResourceNotFoundException if the [Community] does not exist.
     */
    @Modifying
    @Transactional
    override fun hide(community: Community) {
        community.hidden = true
    }

    /**
     * Sets the hidden status to false of a [Community].
     *
     * @param community The [Community] to be updated.
     * @throws ResourceNotFoundException if the [Community] does not exist.
     */
    @Modifying
    @Transactional
    override fun show(community: Community) {
        community.hidden = false
    }

    /**
     * Updates the user membership in role communities depending on [roles][Role].
     *
     * @param user The [User] to be updated.
     * @param roles The [Set] of [Role] objects to be associated with the [User].
     * @throws ResourceNotFoundException if any of the role [Community] objects do not exist.
     */
    @Modifying
    @Transactional
    override fun updateRolesCommunities(user: User, roles: Set<Role>) {
        roleCommunities.forEach { (role, name) ->
            val community = communityDao.findByName(name)
                .orElseThrow { ResourceNotFoundException(Community::class.java) }
            val userInCommunity = userCommunityDao.existsByUserAndCommunity(user, community)
            if (role in roles && !userInCommunity) {
                userCommunityDao.save(UserCommunity(user, community))
            } else if (role !in roles && userInCommunity) {
                userCommunityDao.deleteByUserAndCommunity(user, community)
            }
        }
    }

    /**
     * Uploads a photo to the community specified by [id].
     *
     * @param id The ID of the community to which to upload the photo.
     * @param file The photo to be uploaded.
     * @return The updated [CommunityResponse] object.
     * @throws ApiError with CONFLICT status if the community is associated with a project.
     */
    @Modifying
    @Transactional
    override fun uploadPhoto(id: Long, file: Part): CommunityResponse {
        val community = findEntityById(id)
        community.photoPath = storageManager.saveImage(file, StoreDir.COMMUNITY, community.photoPath, true)
        return communityMapper.asResponse(community)
    }

    /**
     * Deletes the community specified by [id].
     *
     * @param id The ID of the community to delete.
     * @throws ApiError with CONFLICT status if the community is associated with a project.
     */
    @Modifying
    override fun delete(id: Long) {
        val community = findEntityById(id)
        if (community.role) throw ApiError(HttpStatus.CONFLICT, i18n("exception.community.roleAction"))
        if (community.project != null)
            throw ApiError(HttpStatus.CONFLICT, i18n("exception.community.projectDelete"))
        communityDao.delete(community)
        storageManager.removeImage(community.photoPath, StoreDir.COMMUNITY)
    }

    /**
     * Returns a paginated list of members for the community specified by [id].
     *
     * @param id The ID of the community for which to retrieve the members.
     * @param query The search query used to filter the list of members.
     * @param pageRequest The [PageRequest] used to paginate the list of members.
     * @return A paginated list of [MemberShortResponse] objects that match the provided [id] and [query].
     */
    override fun listMembersForUser(id: Long, query: String, pageRequest: ExtendedPageRequest): PageResponse<MemberResponse> {
        val members = listMembers(id, query, pageRequest.pageable)
        return userCommunityMapper.asPageResponse(members)
    }

    /**
     * Returns a paginated list of members for the community specified by [id].
     *
     * @param id The ID of the community for which to retrieve the members.
     * @param query The search query used to filter the list of members.
     * @param pageRequest The [PageRequest] used to paginate the list of members.
     * @return A paginated list of [MemberShortResponse] objects that match the provided [id] and [query].
     */
    override fun listMembersForAdmin(id: Long, query: String, pageRequest: ExtendedPageRequest): PageResponse<MemberResponse> {
        val members = listMembers(id, query, pageRequest.pageable, showHiddenCommunities = true, showBannedUsers = true)
        return userCommunityMapper.asPageResponse(members)
    }

    /**
     * Allows the user join or leave a community.
     *
     * @param userId The ID of the user.
     * @param communityId The ID of the community.
     * @param request The [ParticipationDto] containing the participation request.
     * @param allowProject If true, allows participation in communities associated with projects.
     * @return A [MemberShortResponse] if the user joins the community, or null if the user leaves the community.
     * @throws ApiError with [HttpStatus.CONFLICT] if the community is associated with a project
     * and [allowProject] is false.
     */
    @Modifying
    @Transactional
    override fun participate(
        userId: Long, communityId: Long, request: ParticipationDto, allowProject: Boolean
    ): MemberResponse? {
        val community = findEntityById(communityId)
        if (community.role)
            throw ApiError(HttpStatus.CONFLICT, i18n("exception.community.roleParticipation"))
        if (community.project != null && !allowProject)
            throw ApiError(HttpStatus.CONFLICT, i18n("exception.community.projectParticipation"))
        return if (request.participation) join(communityId, userId)
        else leave(communityId, userId).let { null }
    }

    /**
     * Returns whether a community with the provided [communityId] exists.
     *
     * @param communityId The ID of the community.
     * @return True if a community with the provided [communityId] exists, or false otherwise.
     */
    override fun existById(communityId: Long): Boolean {
        return communityDao.existsById(communityId)
    }
}
