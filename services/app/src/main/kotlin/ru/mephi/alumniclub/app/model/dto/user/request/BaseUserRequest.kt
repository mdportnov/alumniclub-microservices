package ru.mephi.alumniclub.app.model.dto.user.request

import ru.mephi.alumniclub.app.controller.validator.blacklist.BlacklistedRolesConstraint
import ru.mephi.alumniclub.app.controller.validator.daterange.LocalDateRangeConstraint
import ru.mephi.alumniclub.app.controller.validator.degree.DegreeConstraint
import ru.mephi.alumniclub.app.controller.validator.rolesdegrees.RolesAndDegreesConstraint
import ru.mephi.alumniclub.app.model.dto.user.DegreeDTO
import ru.mephi.alumniclub.app.model.enumeration.user.Role
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import javax.validation.Valid
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.Size

@RolesAndDegreesConstraint
abstract class BaseUserRequest(
    @field:Size(max = 30)
    val surname: String,

    @field:Size(max = 30)
    val name: String,

    @field:Size(max = 30)
    val patronymic: String? = null,

    val gender: Boolean,

    @LocalDateRangeConstraint(lowerBound = -120, upperBound = -12, units = ChronoUnit.YEARS)
    val birthday: LocalDate,

    @field:Size(max = 15)
    val phone: String? = null,

    @field:Size(max = 60)
    val vk: String? = null,

    @field:Size(max = 60)
    val tg: String? = null,

    @field:NotEmpty
    @BlacklistedRolesConstraint(values = [Role.ADMIN, Role.MODERATOR, Role.MENTOR])
    val roles: Set<Role>,

    @field:NotEmpty
    @DegreeConstraint
    @field:Valid
    val degrees: List<DegreeDTO>,

    @field:Valid
    val bio: BioRequest?
)