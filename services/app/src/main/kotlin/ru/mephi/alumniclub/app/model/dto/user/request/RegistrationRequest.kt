package ru.mephi.alumniclub.app.model.dto.user.request

import ru.mephi.alumniclub.app.model.dto.user.DegreeDTO
import ru.mephi.alumniclub.app.model.enumeration.user.Role
import ru.mephi.alumniclub.shared.util.emailRegexp
import java.time.LocalDate
import javax.validation.constraints.Pattern
import javax.validation.constraints.Size

class RegistrationRequest(
    @field:Pattern(regexp = emailRegexp)
    @field:Size(min = 4, max = 120)
    val email: String,
    @field:Size(min = 8, max = 255)
    val password: String,
    surname: String,
    name: String,
    patronymic: String? = null,
    gender: Boolean,
    birthday: LocalDate,
    phone: String? = null,
    vk: String? = null,
    tg: String? = null,
    roles: Set<Role>,
    degrees: List<DegreeDTO> = emptyList(),
    bio: BioRequest
) : BaseUserRequest(
    surname, name, patronymic, gender, birthday, phone, vk, tg, roles, degrees, bio
)