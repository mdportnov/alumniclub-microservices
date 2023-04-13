package ru.mephi.alumniclub.app.model.dto.registration_limiter

import javax.validation.constraints.Positive
import javax.validation.constraints.PositiveOrZero

class RegistrationLimiterSettingsRequest(
    @field:PositiveOrZero
    val deltaTime: Long,
    @field:Positive
    val deltaCount: Long,
)