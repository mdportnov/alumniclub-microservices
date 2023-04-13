package ru.mephi.alumniclub.app.model.dto.auth.password.request

import ru.mephi.alumniclub.shared.util.emailRegexp
import javax.validation.constraints.Pattern
import javax.validation.constraints.Size

class RefreshPasswordByEmailRequest(
    @field:Pattern(regexp = emailRegexp) @field:Size(min = 4, max = 120)
    val email: String,
    @field:Size(min = 8, max = 255)
    val oldPassword: String,
    newPassword: String
) : PasswordRequest(newPassword)
