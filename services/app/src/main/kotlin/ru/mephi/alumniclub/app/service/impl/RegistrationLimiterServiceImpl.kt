package ru.mephi.alumniclub.app.service.impl

import com.hazelcast.core.HazelcastInstance
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import ru.mephi.alumniclub.app.database.entity.registration_limiter.RegistrationLimiterSettings
import ru.mephi.alumniclub.app.database.repository.RegistrationLimiterDao
import ru.mephi.alumniclub.app.model.dto.FeatureToggleUpdateRequest
import ru.mephi.alumniclub.app.model.dto.registration_limiter.RegistrationLimiterSettingsRequest
import ru.mephi.alumniclub.app.model.dto.registration_limiter.RegistrationLimiterSettingsResponse
import ru.mephi.alumniclub.app.model.mapper.registraion_limiter.RegistrationLimiterMapper
import ru.mephi.alumniclub.app.service.FeatureToggleService
import ru.mephi.alumniclub.app.service.RegistrationLimiterService
import ru.mephi.alumniclub.shared.util.response.ResponseManager
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicLong

@Service
class RegistrationLimiterServiceImpl(
    private val toggleFeatureToggleService: FeatureToggleService,
    private val dao: RegistrationLimiterDao,
    private val mapper: RegistrationLimiterMapper,
    @Qualifier("HzInstance")
    hazelcastInstance: HazelcastInstance,
    @Value("\${registration-limiter.deltaTime}")
    defaultDeltaTime: Long,
    @Value("\${registration-limiter.deltaCount}")
    defaultDeltaCount: Long
) : ResponseManager(), RegistrationLimiterService {
    private val id = AtomicLong()
    private val map = hazelcastInstance.getMap<Long, String>(baseKey)
    private val defaultSettings = RegistrationLimiterSettings(defaultDeltaTime, defaultDeltaCount)


    /**
     * This function is called when a new user registers.
     * For each registration request, a token is created that lives for [deltaTime] seconds.
     * If there are more than [deltaCount] tokens during registration, the registration toggle feature is turned off
     */
    override fun onRegistration() {
        if (!toggleFeatureToggleService.isFeatureEnabled("registrationLimiter")) return
        val settings = getSettings()
        map.put(id.getAndIncrement(), "", settings.deltaTime, TimeUnit.SECONDS)
        if (map.size > settings.deltaCount) {
            toggleFeatureToggleService.changeFeatureState(
                FeatureToggleUpdateRequest("registration", false)
            )
        }
    }

    /**
     * Returning actual setting from DB.
     * If DB is not available, returns default settings
     */
    override fun findSettings(): RegistrationLimiterSettings {
        return try {
            dao.findByKey(settingsKey).orElseGet { defaultSettings }
        } catch (e: Exception) {
            e.printStackTrace()
            defaultSettings
        }
    }

    /**
     * Returning actual setting as [RegistrationLimiterSettingsResponse]
     */
    override fun getSettings(): RegistrationLimiterSettingsResponse {
        return mapper.asResponse(findSettings())
    }

    /**
     * Save settings to DB
     */
    override fun save(settings: RegistrationLimiterSettings): RegistrationLimiterSettings {
        return dao.save(settingsKey, settings)
    }

    /**
     * Update settings. Removes all registration tokens
     */
    override fun setSettings(settings: RegistrationLimiterSettingsRequest): RegistrationLimiterSettingsResponse {
        val entity = try {
            map.clear()
            save(mapper.asEntity(settings))
        } catch (e: Exception) {
            defaultSettings
        }
        return mapper.asResponse(entity)
    }

    companion object {
        private const val baseKey = "registrationLimiter"
        private const val settingsKey = "$baseKey:settings"
    }
}