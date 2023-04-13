package ru.mephi.alumniclub.app.util.version

import org.springframework.http.HttpStatus
import ru.mephi.alumniclub.shared.dto.common.ApiError

class ApiVersionParseException(
    fullVersion: String
) : ApiError(
    status = HttpStatus.BAD_REQUEST,
    message = "Некорректный формат версии приложения: [$fullVersion]",
    debugMessage = "Incorrect application version format: [$fullVersion]"
)