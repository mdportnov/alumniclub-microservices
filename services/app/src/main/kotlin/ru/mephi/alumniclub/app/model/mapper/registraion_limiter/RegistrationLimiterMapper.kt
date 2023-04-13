package ru.mephi.alumniclub.app.model.mapper.registraion_limiter

import org.springframework.stereotype.Component
import ru.mephi.alumniclub.app.database.entity.registration_limiter.RegistrationLimiterSettings
import ru.mephi.alumniclub.app.model.dto.registration_limiter.RegistrationLimiterSettingsRequest
import ru.mephi.alumniclub.app.model.dto.registration_limiter.RegistrationLimiterSettingsResponse

@Component
class RegistrationLimiterMapper {
    fun asEntity(request: RegistrationLimiterSettingsRequest): RegistrationLimiterSettings {
        return RegistrationLimiterSettings(
            deltaTime = request.deltaTime,
            deltaCount = request.deltaCount
        )
    }

    fun asResponse(entity: RegistrationLimiterSettings): RegistrationLimiterSettingsResponse {
        return RegistrationLimiterSettingsResponse(
            deltaTime = entity.deltaTime,
            deltaCount = entity.deltaCount
        )
    }
}