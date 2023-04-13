package ru.mephi.alumniclub.app.model.dto.user.request

import ru.mephi.alumniclub.app.model.dto.user.DegreeDTO
import ru.mephi.alumniclub.app.model.enumeration.user.Role
import java.time.LocalDate

class UpdateUserRequest(
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
    bio: BioRequest?
) : BaseUserRequest(
    surname, name, patronymic, gender, birthday, phone, vk, tg, roles, degrees, bio
)