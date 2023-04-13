package ru.mephi.alumniclub.app.database.entity.registration_limiter

import java.io.Serializable

class RegistrationLimiterSettings(
    val deltaTime: Long,
    val deltaCount: Long,
) : Serializable