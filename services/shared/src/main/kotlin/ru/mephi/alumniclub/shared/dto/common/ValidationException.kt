package ru.mephi.alumniclub.shared.dto.common

import org.springframework.http.HttpStatus
import javax.validation.ConstraintViolation

class ValidationException(
    constraints: Set<ConstraintViolation<*>>
) : ApiError(
    status = HttpStatus.BAD_REQUEST,
    validationErrors = constraintViolationToValidationErrors(constraints),
    message = "Ошибка валидации",
    debugMessage = "Invalid input data",
) {
    override var message: String = validationErrors.joinToString("\n") {
        if (it.field.isNotEmpty()) "${it.field}: ${it.description}"
        else it.description.orEmpty()
    }
}

fun constraintViolationToValidationErrors(
    constraintViolations: Set<ConstraintViolation<*>>
): List<ValidationError> {
    return constraintViolations.map { violation ->
        ValidationError(
            field = violation.propertyPath.toString(),
            rejectedValue = violation.invalidValue,
            description = violation.message
        )
    }
}