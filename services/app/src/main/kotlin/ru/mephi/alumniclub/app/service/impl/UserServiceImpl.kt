package ru.mephi.alumniclub.app.service.impl

import org.springframework.context.annotation.Lazy
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort.Order
import org.springframework.data.jpa.repository.Modifying
import org.springframework.http.HttpStatus
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import ru.mephi.alumniclub.app.database.entity.user.Biography
import ru.mephi.alumniclub.app.database.entity.user.Degree
import ru.mephi.alumniclub.app.database.entity.user.User
import ru.mephi.alumniclub.app.database.repository.user.*
import ru.mephi.alumniclub.app.model.dto.PageResponse
import ru.mephi.alumniclub.app.model.dto.auth.ClientDeviceDTO
import ru.mephi.alumniclub.app.model.dto.community.response.CommunityResponse
import ru.mephi.alumniclub.app.model.dto.user.UserIdUserEmailPair
import ru.mephi.alumniclub.app.model.dto.user.BioVisibilityDTO
import ru.mephi.alumniclub.app.model.dto.user.DegreeDTO
import ru.mephi.alumniclub.app.model.dto.user.UserExportDTO
import ru.mephi.alumniclub.app.model.dto.user.UserVisibilityDTO
import ru.mephi.alumniclub.app.model.dto.user.request.*
import ru.mephi.alumniclub.app.model.dto.user.response.*
import ru.mephi.alumniclub.app.model.enumeration.user.Role
import ru.mephi.alumniclub.app.model.exception.common.ResourceNotFoundException
import ru.mephi.alumniclub.app.model.mapper.user.*
import ru.mephi.alumniclub.app.service.*
import ru.mephi.alumniclub.app.util.extension.addProtectedRoles
import ru.mephi.alumniclub.shared.dto.ExtendedPageRequest
import ru.mephi.alumniclub.shared.dto.common.ApiError
import ru.mephi.alumniclub.shared.model.enums.StoreDir
import ru.mephi.alumniclub.shared.util.response.ResponseManager
import javax.servlet.http.Part
import javax.transaction.Transactional

@Service
@Transactional
class UserServiceImpl(
    // Manager
    private val storageManager: StorageManager,

    // DAOs
    private val userDao: UserDao,
    private val userVisibilityDao: UserVisibilityDao,
    private val bioDao: BioDao,
    private val bioVisibilityDao: BioVisibilityDao,
    private val degreeDao: DegreeDao,
    private val mentorDao: MentorDao,
    private val fingerprintDao: FingerprintDao,

    // Mappers
    private val userMapper: UserMapper,
    private val bioMapper: BioMapper,
    private val degreeMapper: DegreeMapper,
    private val userVisibilityMapper: UserVisibilityMapper,
    private val bioVisibilityMapper: BioVisibilityMapper,
    private val fingerprintMapper: FingerprintMapper,

    // Services
    @Lazy private val passwordEncoder: PasswordEncoder,
    @Lazy private val communityService: CommunityService

) : ResponseManager(), UserService {
    /**
     * Finds a [User] entity by its id.
     *
     * @param id The id of the [User] to find.
     * @return The [User] entity with the given id.
     * @throws ResourceNotFoundException If the [User] with the given id is not found.
     */
    override fun findUserEntityById(id: Long): User {
        return userDao.findById(id).orElseThrow { ResourceNotFoundException(User::class.java, id) }
    }

    /**
     * Finds a [User] entity by its email.
     *
     * @param email The email of the [User] to find.
     * @return The [User] entity with the given email.
     * @throws ApiError If the [User] with the given email is not found.
     */
    override fun findUserEntityByEmail(email: String): User {
        return userDao.findByEmail(email)
            .orElseThrow { ApiError(HttpStatus.NOT_FOUND, i18n("exception.notFound.userByEmail", email)) }
    }

    /**
     * Finds a list of [UserIdUserEmailPair] for the given list of user ids.
     *
     * @param usersIds The list of user ids.
     * @return The list of [UserIdUserEmailPair].
     */
    override fun findUserIdUserEmailPairs(usersIds: List<Long>): List<UserIdUserEmailPair> {
        return userDao.findUserIdUserEmailPairs(usersIds)
    }

    /**
     * Determines if a [User] is banned.
     *
     * @param id The id of the [User] to check.
     * @return True if the [User] is banned, false otherwise.
     */
    override fun isUserBanned(id: Long) = findUserEntityById(id).banned

    /**
     * Determines if a [User] is an admin.
     *
     * @param id The id of the [User] to check.
     * @return True if the [User] is an admin, false otherwise.
     */
    override fun isUserAdmin(id: Long) = findUserEntityById(id).admin

    /**
     * Determines if a [User] exists by its id.
     *
     * @param id The id of the [User] to check.
     * @return True if the [User] exists, false otherwise.
     */
    override fun isUserExistById(id: Long) = userDao.existsById(id)

    /**
     * Creates a [User] from a [RegistrationRequest].
     *
     * @param request The [RegistrationRequest] to create the [User] from.
     * @return The created [User].
     */
    @Modifying
    @Transactional
    override fun createUser(request: RegistrationRequest): User {
        val user = userMapper.asEntity(request).apply {
            hash = passwordEncoder.encode(request.password)
            userDao.save(this)
        }
        val bio = bioDao.save(bioMapper.asEntity(user, request.bio ?: BioRequest()))
        userVisibilityDao.save(userVisibilityMapper.asEntity(user, UserVisibilityDTO()))
        val bioVisibility = bioVisibilityMapper.asEntity(bio, BioVisibilityDTO())
        bioVisibilityDao.save(bioVisibility)
        updateDegrees(user, request.degrees)
        updateRoles(user, request.roles)
        return user
    }


    /**
     * Uploads a photo for a [User].
     *
     * @param id The id of the [User] to upload the photo for.
     * @param file The photo to upload.
     * @return The [UserResponse] with the updated photo path.
     */
    @Modifying
    override fun uploadPhoto(id: Long, file: Part): UserResponse {
        val user = findUserEntityById(id)
        val bio = bioDao.findByUserId(user.id)
        user.photoPath = storageManager.saveImage(file, StoreDir.USER, user.photoPath)
        return userMapper.asUserResponse(user, degreeDao.findByUserId(user.id), bio)
    }

    /**
     * Gets a [UserResponse] for a [User].
     *
     * @param id The id of the [User] to get the [UserResponse] for.
     * @param visibilityCheck If the visibility checks should be ignored.
     * @return The [UserResponse].
     * @throws ApiError If the [User] is banned and the visibility checks are not ignored.
     */
    override fun getUserById(id: Long, visibilityCheck: Boolean): UserResponse {
        val user = findUserEntityById(id)
        if (visibilityCheck && user.banned)
            throw ApiError(HttpStatus.FORBIDDEN, i18n("exception.forbidden.userBanned"))
        val degrees = degreeDao.findByUserId(user.id)
        val bio = bioDao.findByUserId(user.id)
        return if (visibilityCheck) userMapper.asUserResponseMasked(user, degrees, bio)
        else userMapper.asUserResponse(user, degrees, bio)
    }

    /**
     * Gets a [BioResponse] for a [User].
     *
     * @param id The id of the [User] to get the [BioResponse] for.
     * @return The [BioResponse].
     */
    override fun getUserBioById(id: Long): BioResponse {
        val bio = bioDao.findByUserId(id)
        return bioMapper.asResponse(bio)
    }

    /**
     * Gets a preview of the [User] with the given ID.
     *
     * @param id the ID of the [User]
     * @return a [UserPreviewResponse] containing the information about the [User]
     */
    override fun getUserPreviewById(id: Long): UserPreviewResponse {
        val user = findUserEntityById(id)
        return userMapper.asPreviewResponse(user)
    }

    /**
     * Gets the visibility information of the [User] with the given ID.
     *
     * @param id the ID of the [User]
     * @return a [UserVisibilityDTO] containing the visibility information of the [User]
     * @throws ResourceNotFoundException if the [User] with the given ID cannot be found
     */
    override fun getUserVisibility(id: Long): UserVisibilityDTO {
        val bioVisibility =
            bioVisibilityDao.findById(id).orElseThrow { ResourceNotFoundException(User::class.java, id) }
        val userVisibility =
            userVisibilityDao.findById(id).orElseThrow { ResourceNotFoundException(User::class.java, id) }
        return userVisibilityMapper.asResponse(userVisibility, bioVisibility)
    }

    /**
     * Gets the list of devices used by [User] with the given ID.
     *
     * @param id the ID of the [User]
     * @return a [UserVisibilityDTO] containing the visibility information of the [User]
     * @throws ResourceNotFoundException if the [User] with the given ID cannot be found
     */
    override fun getUserDevices(id: Long): List<ClientDeviceDTO> {
        val fingerprints = fingerprintDao.findByUserId(id)
        return fingerprintMapper.asListResponse(fingerprints)
    }

    /**
     * Gets the list of devices except device with [deviceId] used by [User] with the given ID.
     *
     * @param id the ID of the [User]
     * @param deviceId the fingerprint of device to exclude
     * @return a [UserVisibilityDTO] containing the visibility information of the [User]
     * @throws ResourceNotFoundException if the [User] with the given ID cannot be found
     */
    override fun getUserDevices(id: Long, deviceId: String): List<ClientDeviceDTO> {
        val fingerprints = fingerprintDao.findByUserIdAndDeviceIdNot(id, deviceId)
        return fingerprintMapper.asListResponse(fingerprints)
    }

    /**
     * Updates the ban status of the [User] with the given ID.
     *
     * @param id the ID of the [User]
     * @param request the [BanRequest] containing the new ban status for the [User]
     * @return a [UserResponse] containing the updated information about the [User]
     */
    @Modifying
    override fun updateUserBanStatus(id: Long, request: BanRequest): UserResponse {
        val user = findUserEntityById(id)
        user.banned = request.banned
        val degree = degreeDao.findByUserId(user.id)
        val bio = bioDao.findByUserId(user.id)
        return userMapper.asUserResponse(user, degree, bio)
    }

    /**
     * Updates the profile information of the [User] with the given ID.
     *
     * @param id the ID of the [User]
     * @param request the [UpdateUserRequest] containing the updated information for the [User]
     * @return a [UserResponse] containing the updated information about the [User]
     */
    @Modifying
    @Transactional
    override fun updateProfile(id: Long, request: UpdateUserRequest): UserResponse {
        val user = findUserEntityById(id)
        val updatedUser = userMapper.update(user, request)
        val bio = request.bio?.let { updateBio(user, it) } ?: bioDao.findByUserId(id)
        val degrees = updateDegrees(user, request.degrees)
        updateRoles(user, request.roles.addProtectedRoles(user))
        return userMapper.asUserResponse(updatedUser, degrees, bio)
    }

    /**
     * Updates the [Degree]s for the specified [User].
     *
     * @param user The [User] whose degrees should be updated.
     * @param newDegrees A list of [DegreeDTO] objects containing the updated degrees for the user.
     * @return A list of [Degree] objects for the updated user.
     */
    @Modifying
    private fun updateDegrees(user: User, newDegrees: List<DegreeDTO>): List<Degree> {
        val userDegreesMap = degreeDao.findByUserId(user.id).associateBy { degreeMapper.asDTO(it) }

        val degreesToDelete = userDegreesMap.filterKeys { !newDegrees.contains(it) }.values
        degreesToDelete.forEach { degreeDao.delete(it) }

        val degreesToCreate =
            newDegrees.filterNot { userDegreesMap.containsKey(it) }.map { degreeMapper.asEntity(user, it) }

        degreesToCreate.forEach(degreeDao::save)

        val createdDegrees = degreeDao.findByUserId(user.id).associateBy { degreeMapper.asDTO(it) }

        return createdDegrees.values.toList()
    }

    /**
     * Updates the [Role]s for the specified [User].
     *
     * @param user The [User] whose roles should be updated.
     * @param roles The updated [Role] set for the user.
     */
    override fun updateRoles(user: User, roles: Set<Role>) {
        user.updateRoles(roles)
        communityService.updateRolesCommunities(user, roles)
    }

    /**
     * Updates the bio of the [User] with the given ID.
     *
     * @param id the ID of the [User]
     * @param request the [BioRequest] containing the updated information for the [User]'s bio
     * @return a [BioResponse] containing the updated information about the [User]'s bio
     */
    @Modifying
    override fun updateBio(id: Long, request: BioRequest): BioResponse? {
        val user = findUserEntityById(id)
        val bio = updateBio(user, request)
        return bioMapper.asResponse(bio)
    }

    /**
     * Updates the visibility information of the [User] with the given ID.
     *
     * @param id the ID of the [User]
     * @param request the [UserVisibilityDTO] containing the updated visibility information for the [User]
     * @return a [UserVisibilityDTO] containing the updated visibility information of the [User]
     */
    @Modifying
    @Transactional
    override fun updateVisibility(id: Long, request: UserVisibilityDTO): UserVisibilityDTO {
        val user = findUserEntityById(id)

        val userVisibility = userVisibilityDao.findById(user.id).map {
            userVisibilityMapper.update(it, request)
        }.orElseGet {
            val userVisibility = userVisibilityMapper.asEntity(user, request)
            userVisibilityDao.save(userVisibility)
        }

        val bioVisibility = bioVisibilityMapper.update(bioVisibilityDao.findByUserId(user.id), request.bioVisibility)
        return userVisibilityMapper.asResponse(userVisibility, bioVisibility)
    }

    /**
     * Lists [User]s that have a surname starting with the given query.
     *
     * @param query the query string
     * @param pageRequest the [ExtendedPageRequest] containing the information for pagination
     * @return a [PageResponse] of [UserPreviewResponse] containing the [User]s matching the query
     */
    override fun list(query: String, pageRequest: ExtendedPageRequest): PageResponse<UserPreviewResponse> {
        val page = if (pageRequest.field == "fullName") generateFullNamePagination(pageRequest) else pageRequest
        val users = userDao.findBySurnameStartsWith(query, page.pageable)
        return userMapper.asUserPreviewPageResponse(users)
    }

    /**
     * Lists [User]s that are moderators or administrators and have a surname starting with the given query.
     *
     * @param query the query string
     * @param pageRequest the [ExtendedPageRequest] containing the information for pagination
     * @return a [Page] of [User] containing the [User]s matching the query
     */
    override fun listByModeratorOrAdminAndSurnameStartsWith(
        query: String, pageRequest: ExtendedPageRequest
    ): Page<User> {
        return userDao.findByModeratorTrueOrAdminTrueAndSurnameStartsWith(query, pageRequest.pageable)
    }

    /**
     * Retrieves a paginated list of communities for the specified user.
     *
     * @param id The ID of the user.
     * @param pageRequest The [PageRequest] specifying the page number and size.
     * @return A [PageResponse] of [CommunityResponse] objects for the user's communities.
     */
    override fun listUserCommunities(id: Long, pageRequest: ExtendedPageRequest): PageResponse<CommunityResponse> {
        return communityService.listByUser(userId = id, pageRequest = pageRequest, includeHidden = true)
    }


    /**
     * Exports information for all users as [UserExportDTO] objects.
     *
     * @return An iterable of [UserExportDTO] objects for all users.
     */
    override fun export(): Iterable<UserExportDTO> {
        val users = userDao.findAll()
        return userMapper.asExportList(users)
    }

    /**
     * Deletes the specified user.
     *
     * @param id The ID of the user to delete.
     * @throws ApiError with status `FORBIDDEN` when attempting to delete an admin user.
     */
    @Modifying
    override fun delete(id: Long) {
        val user = findUserEntityById(id)
        if (user.admin) throw ApiError(HttpStatus.FORBIDDEN, i18n("exception.forbidden.cantDeleteAdmin"))
        deleteUser(user)
    }

    /**
     * Deletes the specified user.
     *
     * @param user The [User] to delete.
     */
    @Modifying
    @Transactional(Transactional.TxType.REQUIRES_NEW)
    fun deleteUser(user: User) {
        storageManager.removeImage(user.photoPath, StoreDir.USER)
        bioVisibilityDao.deleteBioVisibilityByUserId(user.id)
        bioDao.deleteBiographyByUserId(user.id)
        userVisibilityDao.deleteUserVisibilityByUserId(user.id)
        userDao.deleteUserById(user.id)
    }

    /**
     * Updates the biography for the specified [User].
     *
     * @param user The [User] whose biography should be updated.
     * @param bioRequest The [BioRequest] containing the updated biography information.
     * @return The updated [Biography] for the user.
     */
    @Modifying
    private fun updateBio(user: User, bioRequest: BioRequest): Biography {
        val bio = bioDao.findByUserId(user.id)
        return bioMapper.update(bio, bioRequest)
    }

    /**
     * Generates a [ExtendedPageRequest] for full name pagination.
     *
     * @param page The original [ExtendedPageRequest].
     * @return A [ExtendedPageRequest] with orders for pagination by full name.
     */
    private fun generateFullNamePagination(page: ExtendedPageRequest): ExtendedPageRequest {
        val orders = listOf(Order(page.order, "surname"), Order(page.order, "name"))
        page.updateRequest(orders)
        return page
    }
}
