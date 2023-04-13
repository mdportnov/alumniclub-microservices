package ru.mephi.alumniclub.app.model.dto.auth

import ru.mephi.alumniclub.app.model.enumeration.user.ClientType
import javax.validation.constraints.Size

class ClientDeviceDTO(
    @field:Size(max = 300)
    val name: String? = null,
    @field:Size(max = 300)
    val version: String? = null,
    val type: ClientType? = null
)
