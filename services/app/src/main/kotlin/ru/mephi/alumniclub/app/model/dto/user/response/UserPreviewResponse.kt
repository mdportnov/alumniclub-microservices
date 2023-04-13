package ru.mephi.alumniclub.app.model.dto.user.response

import ru.mephi.alumniclub.app.model.enumeration.user.Role
import ru.mephi.alumniclub.shared.dto.AbstractCreatedAtResponse
import ru.mephi.alumniclub.shared.dto.photo.PhotoPathed
import java.time.LocalDate
import java.time.LocalDateTime

/**
 * DTO used in responses without additional joins like degrees and bios
 */
class UserPreviewResponse(
    id: Long,
    createdAt: LocalDateTime,
    override var photoPath: String?,
    val fullName: String,
    val surname: String,
    val name: String,
    val patronymic: String?,
    val birthday: LocalDate?,
    val email: String?,
    val gender: Boolean,
    val banned: Boolean,
    val phone: String?,
    val vk: String?,
    val tg: String?,
    val roles: Set<Role>?,
) : AbstractCreatedAtResponse<Long>(id, createdAt), PhotoPathed
