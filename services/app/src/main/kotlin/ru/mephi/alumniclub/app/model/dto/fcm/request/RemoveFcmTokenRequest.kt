package ru.mephi.alumniclub.app.model.dto.fcm.request

import javax.validation.constraints.Size

class RemoveFcmTokenRequest(
    @field:Size(max = 300)
    val fingerprint: String
)
