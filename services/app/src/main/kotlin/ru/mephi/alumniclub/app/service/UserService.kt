package ru.mephi.alumniclub.app.service

import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.data.domain.Page
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestPart
import ru.mephi.alumniclub.app.database.entity.user.User
import ru.mephi.alumniclub.app.model.dto.PageResponse
import ru.mephi.alumniclub.app.model.dto.auth.ClientDeviceDTO
import ru.mephi.alumniclub.app.model.dto.community.response.CommunityResponse
import ru.mephi.alumniclub.app.model.dto.user.UserIdUserEmailPair
import ru.mephi.alumniclub.app.model.dto.user.UserExportDTO
import ru.mephi.alumniclub.app.model.dto.user.UserVisibilityDTO
import ru.mephi.alumniclub.app.model.dto.user.request.BanRequest
import ru.mephi.alumniclub.app.model.dto.user.request.BioRequest
import ru.mephi.alumniclub.app.model.dto.user.request.RegistrationRequest
import ru.mephi.alumniclub.app.model.dto.user.request.UpdateUserRequest
import ru.mephi.alumniclub.app.model.dto.user.response.BioResponse
import ru.mephi.alumniclub.app.model.dto.user.response.UserPreviewResponse
import ru.mephi.alumniclub.app.model.dto.user.response.UserResponse
import ru.mephi.alumniclub.app.model.enumeration.user.Role
import ru.mephi.alumniclub.shared.dto.ExtendedPageRequest
import javax.servlet.http.Part

@Tag(name = "User API")
interface UserService {
    fun findUserEntityById(id: Long): User
    fun findUserEntityByEmail(email: String): User
    fun findUserIdUserEmailPairs(usersIds: List<Long>): List<UserIdUserEmailPair>
    fun isUserBanned(id: Long): Boolean
    fun isUserAdmin(id: Long): Boolean
    fun isUserExistById(id: Long): Boolean
    fun createUser(request: RegistrationRequest): User
    fun uploadPhoto(@Parameter(hidden = true) id: Long, @RequestPart file: Part): UserResponse
    fun getUserById(@PathVariable id: Long, @Parameter(hidden = true) visibilityCheck: Boolean): UserResponse
    fun getUserBioById(@PathVariable id: Long): BioResponse
    fun getUserPreviewById(@PathVariable id: Long): UserPreviewResponse
    fun getUserVisibility(@Parameter(hidden = true) id: Long): UserVisibilityDTO
    fun getUserDevices(@Parameter(hidden = true) id: Long): List<ClientDeviceDTO>
    fun getUserDevices(@Parameter(hidden = true) id: Long, fingerprint: String): List<ClientDeviceDTO>
    fun updateUserBanStatus(@PathVariable id: Long, @RequestBody request: BanRequest): UserResponse
    fun updateProfile(@Parameter(hidden = true) id: Long, @RequestBody request: UpdateUserRequest): UserResponse
    fun updateBio(@Parameter(hidden = true) id: Long, @RequestBody request: BioRequest): BioResponse?
    fun updateRoles(user: User, roles: Set<Role>)
    fun updateVisibility(@Parameter(hidden = true) id: Long, @RequestBody request: UserVisibilityDTO): UserVisibilityDTO
    fun list(query: String = "", pageRequest: ExtendedPageRequest): PageResponse<UserPreviewResponse>
    fun listByModeratorOrAdminAndSurnameStartsWith(query: String, pageRequest: ExtendedPageRequest): Page<User>
    fun listUserCommunities(@PathVariable id: Long, pageRequest: ExtendedPageRequest): PageResponse<CommunityResponse>
    fun export(): Iterable<UserExportDTO>
    fun delete(@PathVariable id: Long)
}
