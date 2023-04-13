package ru.mephi.alumniclub.app.model.dto.auth.password.request

import javax.validation.constraints.Size

open class PasswordRequest(
    @field:Size(min = 8, max = 255)
    val password: String
)
