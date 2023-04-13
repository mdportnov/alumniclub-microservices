package ru.mephi.alumniclub.app.controller.validator.degree

import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext
import ru.mephi.alumniclub.app.model.dto.user.DegreeDTO
import ru.mephi.alumniclub.app.model.enumeration.user.DegreeType
import ru.mephi.alumniclub.shared.util.extension.hasDuplicates
import ru.mephi.alumniclub.shared.util.response.ResponseManager
import java.time.LocalDate
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

class DegreeValidator : ConstraintValidator<DegreeConstraint, List<DegreeDTO>>, ResponseManager() {
    override fun isValid(degrees: List<DegreeDTO>, context: ConstraintValidatorContext): Boolean {
        val errors = validateDTO(degrees)
        val ctx = context.unwrap(HibernateConstraintValidatorContext::class.java)
        if (errors.isEmpty()) return true
        ctx.addMessageParameter("maxEnrollmentYear", LocalDate.now().plusYears(1).year)
            .buildConstraintViolationWithTemplate(errors.joinToString("; "))
            .addConstraintViolation()
            .disableDefaultConstraintViolation()
        return false
    }

    private fun validateDTO(degrees: List<DegreeDTO>): List<String> {
        val errors = mutableListOf<String>()

        if (degrees.map { it.degree }.hasDuplicates()) {
            errors.add(i18n("validation.degrees.duplicated"))
        }

        degrees.forEach { degree ->
            if (degree.enrollment > LocalDate.now().plusYears(1).year)
                errors.add("validation.degrees.enrollmentYearExceed")
            val lowerBound = degree.enrollment + 1
            val upperBound = degree.enrollment + when (degree.degree) {
                DegreeType.WORKER -> 60
                DegreeType.LYCEUM -> 11
                else -> 10
            }
            degree.graduation?.let { graduation ->
                if (graduation !in lowerBound..upperBound) errors.add(
                    i18n("validation.degrees.graduationRange", lowerBound.toString(), upperBound.toString())
                )
            }
        }
        return errors
    }
}