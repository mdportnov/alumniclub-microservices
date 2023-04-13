package ru.mephi.alumniclub.app.model.dto.user.response

import ru.mephi.alumniclub.app.model.dto.user.DegreeDTO
import ru.mephi.alumniclub.app.model.enumeration.user.Role
import ru.mephi.alumniclub.shared.dto.AbstractCreatedAtResponse
import ru.mephi.alumniclub.shared.dto.photo.PhotoPathed
import java.time.LocalDate
import java.time.LocalDateTime

class UserResponse(
    override var id: Long,
    override var createdAt: LocalDateTime,
    override var photoPath: String?,
    val fullName: String,
    val surname: String,
    val name: String,
    val patronymic: String?,
    val gender: Boolean,
    val birthday: LocalDate?,
    val banned: Boolean,
    val email: String?,
    val phone: String?,
    val vk: String?,
    val tg: String?,
    val roles: Set<Role>?,
    val bio: BioResponse?,
    val degrees: List<DegreeDTO>?,
) : AbstractCreatedAtResponse<Long>(id, createdAt), PhotoPathed
