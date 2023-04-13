package ru.mephi.alumniclub.app.model.dto.user.request

import javax.validation.constraints.Size

class BanRequest(
    @field:Size(max = 300)
    val description: String = "",
    val banned: Boolean
)
