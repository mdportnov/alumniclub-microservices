package ru.mephi.alumniclub.app.model.dto.auth.request

import javax.validation.constraints.Size

class RefreshRequest(
    @field:Size(max = 300)
    val fingerprint: String
)