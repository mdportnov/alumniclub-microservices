package ru.mephi.alumniclub.app.model.dto.auth.password.request

import ru.mephi.alumniclub.app.model.dto.auth.ClientDeviceDTO
import javax.validation.Valid

class ResetPasswordRequest(
    val fingerprint: String,
    @field:Valid
    val client: ClientDeviceDTO = ClientDeviceDTO(),
    password: String
) : PasswordRequest(password) {
    lateinit var token: String
}