package ru.mephi.alumniclub.shared.model.exceptions.common

import org.springframework.http.HttpStatus
import ru.mephi.alumniclub.shared.dto.common.ApiError

class CorruptedTokenException : ApiError(
    status = HttpStatus.UNAUTHORIZED,
    message = "Токен повреждён",
    debugMessage = "Token is corrupted"
)
