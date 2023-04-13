package ru.mephi.alumniclub.app.service

import org.springframework.web.bind.annotation.RequestBody
import ru.mephi.alumniclub.app.database.entity.registration_limiter.RegistrationLimiterSettings
import ru.mephi.alumniclub.app.model.dto.registration_limiter.RegistrationLimiterSettingsRequest
import ru.mephi.alumniclub.app.model.dto.registration_limiter.RegistrationLimiterSettingsResponse

interface RegistrationLimiterService {
    fun onRegistration()
    fun findSettings(): RegistrationLimiterSettings
    fun save(settings: RegistrationLimiterSettings): RegistrationLimiterSettings
    fun getSettings(): RegistrationLimiterSettingsResponse
    fun setSettings(@RequestBody settings: RegistrationLimiterSettingsRequest): RegistrationLimiterSettingsResponse
}