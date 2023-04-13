package ru.mephi.alumniclub.app.model.dto.auth.password.request

import javax.validation.constraints.Size

class RefreshPasswordRequest(
    @field:Size(min = 8, max = 255)
    val oldPassword: String,
    newPassword: String
) : PasswordRequest(newPassword)
