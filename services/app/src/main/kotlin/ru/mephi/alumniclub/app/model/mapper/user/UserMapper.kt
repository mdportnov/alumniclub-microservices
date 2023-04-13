package ru.mephi.alumniclub.app.model.mapper.user

import org.springframework.data.domain.Page
import org.springframework.stereotype.Component
import ru.mephi.alumniclub.app.database.entity.user.Biography
import ru.mephi.alumniclub.app.database.entity.user.Degree
import ru.mephi.alumniclub.app.database.entity.user.User
import ru.mephi.alumniclub.app.database.repository.user.*
import ru.mephi.alumniclub.app.model.dto.PageResponse
import ru.mephi.alumniclub.app.model.dto.user.UserExportDTO
import ru.mephi.alumniclub.app.model.dto.user.request.RegistrationRequest
import ru.mephi.alumniclub.app.model.dto.user.request.UpdateUserRequest
import ru.mephi.alumniclub.app.model.dto.user.response.UserPreviewResponse
import ru.mephi.alumniclub.app.model.dto.user.response.UserResponse
import ru.mephi.alumniclub.app.model.dto.user.response.UserShortResponse

@Component
class UserMapper(
    private val bioMapper: BioMapper,
    private val degreeMapper: DegreeMapper,
    private val userVisibilityDao: UserVisibilityDao,
    private val bioVisibilityDao: BioVisibilityDao,
    private val mentorDao: MentorDao,
    private val degreeDao: DegreeDao,
    private val bioDao: BioDao
) {
    fun asEntity(request: RegistrationRequest): User {
        return User(
            request.email,
            request.phone,
            request.vk,
            request.tg,
            request.name,
            request.surname,
            request.patronymic,
            request.gender,
            request.birthday,
        ).apply {
            roles = request.roles
        }
    }

    fun update(entity: User, request: UpdateUserRequest): User {
        entity.surname = request.surname
        entity.name = request.name
        entity.patronymic = request.patronymic
        entity.gender = request.gender
        entity.birthday = request.birthday
        entity.phone = request.phone
        entity.vk = request.vk
        entity.tg = request.tg
        return entity
    }

    /**
     * For registration, self profile get/update, uploading photo
     */
    fun asUserResponse(entity: User, degrees: List<Degree>, bio: Biography? = null): UserResponse {
        return UserResponse(
            id = entity.id,
            createdAt = entity.createdAt,
            photoPath = entity.photoPath,
            fullName = entity.fullName,
            name = entity.name,
            surname = entity.surname,
            patronymic = entity.patronymic,
            gender = entity.gender,
            birthday = entity.birthday,
            banned = entity.banned,
            email = entity.email,
            phone = entity.phone,
            vk = entity.vk,
            tg = entity.tg,
            roles = entity.roles,
            bio = bio?.let { bioMapper.asResponse(it) },
            degrees = degreeMapper.asDTOList(degrees),
        )
    }

    fun asUserResponseMasked(user: User, degrees: List<Degree>, bio: Biography): UserResponse {
        val visibility = userVisibilityDao.findByUserId(user.id)
        val bioVisibility = bioVisibilityDao.findByUserId(user.id)
        return UserResponse(
            id = user.id,
            createdAt = user.createdAt,
            photoPath = user.photoPath,
            name = user.name,
            fullName = user.fullName,
            surname = user.surname,
            patronymic = user.patronymic,
            gender = user.gender,
            birthday = if (visibility.birthday) user.birthday else null,
            banned = user.banned,
            email = if (visibility.email) user.email else null,
            phone = if (visibility.phone) user.phone else null,
            vk = if (visibility.vk) user.vk else null,
            tg = if (visibility.tg) user.tg else null,
            roles = user.roles,
            degrees = if (visibility.degrees) degreeMapper.asDTOList(degrees) else null,
            bio = bioMapper.asMaskedResponse(bio, bioVisibility)
        )
    }

    /**
     * - in getUserPreviewById,
     * - in JoinResponse
     */
    fun asPreviewResponse(entity: User): UserPreviewResponse {
        return UserPreviewResponse(
            id = entity.id,
            createdAt = entity.createdAt,
            photoPath = entity.photoPath,
            fullName = entity.fullName,
            name = entity.name,
            surname = entity.surname,
            patronymic = entity.patronymic,
            gender = entity.gender,
            birthday = entity.birthday,
            banned = entity.banned,
            email = entity.email,
            phone = entity.phone,
            vk = entity.vk,
            tg = entity.tg,
            roles = entity.roles,
        )
    }

    /**
     * For admin API: user list and survey answers
     */
    fun asUserPreviewPageResponse(entities: Page<User>): PageResponse<UserPreviewResponse> {
        return PageResponse(
            entities.content.map { asPreviewResponse(it) },
            entities.number.toLong(),
            entities.numberOfElements.toLong(),
            entities.totalPages.toLong()
        )
    }

    fun asUserPreviewMaskedResponse(user: User): UserPreviewResponse {
        val visibility = userVisibilityDao.findByUserId(user.id)
        return UserPreviewResponse(
            id = user.id,
            createdAt = user.createdAt,
            photoPath = user.photoPath,
            name = user.name,
            fullName = user.fullName,
            surname = user.surname,
            patronymic = user.patronymic,
            gender = user.gender,
            birthday = if (visibility.birthday) user.birthday else null,
            banned = user.banned,
            email = if (visibility.email) user.email else null,
            phone = if (visibility.phone) user.phone else null,
            vk = if (visibility.vk) user.vk else null,
            tg = if (visibility.tg) user.tg else null,
            roles = user.roles,
        )
    }

    /**
     * For all kind of responses with other entities includes user info
     */
    fun asShortResponse(entity: User): UserShortResponse {
        return UserShortResponse(id = entity.id, fullName = entity.fullName, photoPath = entity.photoPath)
    }

    /**
     * For all kind of responses with other entities includes user info
     */
    fun asNullableShortResponse(entity: User?): UserShortResponse? {
        return if (entity == null) null else asShortResponse(entity)
    }

    fun asExportList(users: Iterable<User>) = users.map(::asExportDto)

    private fun asExportDto(user: User): UserExportDTO {
        val degrees = degreeDao.findByUserId(user.id)
        return UserExportDTO(
            user = user,
            degrees = degreeMapper.asDTOList(degrees),
            bio = bioDao.findByUserId(user.id),
            mentor = mentorDao.findByUserId(user.id)
        )
    }
}
