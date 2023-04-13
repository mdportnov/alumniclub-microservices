package ru.mephi.alumniclub.shared.model.exceptions.common

import org.springframework.http.HttpStatus
import ru.mephi.alumniclub.shared.dto.common.ApiError

class MissingParameterException(
    parameter: String,
) : ApiError(
    status = HttpStatus.BAD_REQUEST,
    message = "Отсутствующий параметр [$parameter]",
    debugMessage = "Missing parameter [$parameter]"
)
