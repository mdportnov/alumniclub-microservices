package ru.mephi.alumniclub.shared.model.exceptions.common

import org.springframework.http.HttpStatus
import ru.mephi.alumniclub.shared.dto.common.ApiError

class PrincipalNotFoundException : ApiError(
    status = HttpStatus.UNAUTHORIZED,
    message = "Пользователь не авторизован",
    debugMessage = "User is not authorized"
)
