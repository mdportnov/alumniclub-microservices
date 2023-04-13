package ru.mephi.alumniclub.app.model.dto.auth.password.request

import ru.mephi.alumniclub.shared.util.emailRegexp
import javax.validation.constraints.Pattern
import javax.validation.constraints.Size

class SendEmailToResetPasswordRequest(
    @field:Pattern(regexp = emailRegexp)
    @field:Size(min = 4, max = 120)
    val email: String
)