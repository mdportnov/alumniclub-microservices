package ru.mephi.alumniclub.shared.model.exceptions.common

import org.springframework.http.HttpStatus
import ru.mephi.alumniclub.shared.dto.common.ApiError

class MissingPathVariableException(
    variable: String
) : ApiError(
    status = HttpStatus.BAD_REQUEST,
    message = "Переменная пути [$variable] не предоставлена",
    debugMessage = "Path variable [$variable] is not provided"
)
