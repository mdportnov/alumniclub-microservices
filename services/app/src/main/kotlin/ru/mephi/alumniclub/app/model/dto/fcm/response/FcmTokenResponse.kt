package ru.mephi.alumniclub.app.model.dto.fcm.response

import java.time.LocalDateTime

class FcmTokenResponse(
    val userId: Long = -1,
    val fingerprint: String = "",
    val token: String = "",
    val isValid: Boolean = false,
    val updateTime: LocalDateTime? = null
)