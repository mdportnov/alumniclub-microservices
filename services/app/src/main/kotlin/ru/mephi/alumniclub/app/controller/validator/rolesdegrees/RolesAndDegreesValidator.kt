package ru.mephi.alumniclub.app.controller.validator.rolesdegrees

import ru.mephi.alumniclub.app.model.dto.user.DegreeDTO
import ru.mephi.alumniclub.app.model.dto.user.request.BaseUserRequest
import ru.mephi.alumniclub.app.model.enumeration.user.DegreeType
import ru.mephi.alumniclub.app.model.enumeration.user.Role
import ru.mephi.alumniclub.shared.util.extension.containsAny
import ru.mephi.alumniclub.shared.util.response.ResponseManager
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

class RolesAndDegreesValidator : ConstraintValidator<RolesAndDegreesConstraint, BaseUserRequest>, ResponseManager() {
    override fun initialize(constraintAnnotation: RolesAndDegreesConstraint) {
        super.initialize(constraintAnnotation)
    }

    override fun isValid(userRequest: BaseUserRequest, context: ConstraintValidatorContext): Boolean {
        val errors = validateRolesAndDegrees(userRequest.degrees, userRequest.roles)
        val isValid = errors.isEmpty()
        return if (isValid) {
            true
        } else {
            context.disableDefaultConstraintViolation()
            context.buildConstraintViolationWithTemplate(errors.joinToString("; "))
                .addConstraintViolation()
            false
        }
    }

    private fun validateRolesAndDegrees(newDegrees: List<DegreeDTO>, newRoles: Set<Role>): List<String> {
        val degreeTypes = newDegrees.map { it.degree }
        val errors = mutableListOf<String>()

        val validations = listOf(
            newRoles.contains(Role.LYCEUM) && !degreeTypes.contains(DegreeType.LYCEUM),
            newRoles.contains(Role.WORKER) && !degreeTypes.contains(DegreeType.WORKER),
            newRoles.containsAny(Role.STUDENT, Role.ALUMNUS) && !degreeTypes.containsAny(
                DegreeType.BACHELOR, DegreeType.MASTER, DegreeType.SPECIALTY, DegreeType.POSTGRADUATE
            ),
            degreeTypes.any { degree ->
                degree == DegreeType.LYCEUM && !newRoles.contains(Role.LYCEUM) ||
                        degree == DegreeType.WORKER && !newRoles.contains(Role.WORKER) ||
                        degree in setOf(
                    DegreeType.BACHELOR,
                    DegreeType.MASTER,
                    DegreeType.SPECIALTY,
                    DegreeType.POSTGRADUATE
                ) && !newRoles.containsAny(Role.STUDENT, Role.ALUMNUS)
            })

        if (validations[0]) errors.add(i18n("validation.roles.lyceumWithoutDegree"))
        if (validations[1]) errors.add(i18n("validation.roles.workerWithoutDegree"))
        if (validations[2]) errors.add(i18n("validation.roles.studentAlumnusWithoutDegree"))
        if (validations[3]) errors.add(i18n("validation.roles.degreeWithoutRole"))

        return errors
    }
}