package ru.mephi.alumniclub.app.model.dto.fcm.request

import javax.validation.constraints.Size

class UploadTokenRequest(
    @field:Size(max = 300)
    var fingerprint: String,
    @field:Size(max = 300)
    var token: String
)