package ru.mephi.alumniclub.app.model.dto.auth.request

import ru.mephi.alumniclub.app.model.dto.auth.ClientDeviceDTO
import ru.mephi.alumniclub.shared.util.emailRegexp
import javax.validation.Valid
import javax.validation.constraints.Pattern
import javax.validation.constraints.Size

class LoginRequest(
    @field:Pattern(regexp = emailRegexp)
    @field:Size(min = 4, max = 120)
    val email: String,
    @field:Size(min = 8, max = 255)
    val password: String,
    @field:Size(max = 300)
    val fingerprint: String,
    @field:Valid
    val client: ClientDeviceDTO = ClientDeviceDTO()
)