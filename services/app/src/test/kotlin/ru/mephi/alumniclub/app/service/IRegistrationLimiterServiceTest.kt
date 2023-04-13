package ru.mephi.alumniclub.app.service

import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.TestPropertySource
import ru.mephi.alumniclub.app.model.dto.FeatureToggleUpdateRequest
import ru.mephi.alumniclub.app.model.dto.registration_limiter.RegistrationLimiterSettingsRequest
import ru.mephi.alumniclub.app.model.dto.registration_limiter.RegistrationLimiterSettingsResponse
import ru.mephi.alumniclub.app.service.impl.FeatureToggleServiceImpl
import ru.mephi.alumniclub.app.service.impl.RegistrationLimiterServiceImpl

@SpringBootTest
@TestPropertySource(locations = ["classpath:application-test.yml"])
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class IRegistrationLimiterServiceTest {
    lateinit var startSettings: RegistrationLimiterSettingsResponse

    @Autowired
    lateinit var limiter: RegistrationLimiterServiceImpl

    @Autowired
    lateinit var toggleService: FeatureToggleServiceImpl

    @BeforeAll
    fun beforeAll() {
        startSettings = limiter.getSettings()
    }

    @AfterAll
    fun afterAll() {
        limiter.setSettings(RegistrationLimiterSettingsRequest(startSettings.deltaTime, startSettings.deltaCount))
    }


    @Test
    fun `get settings`() {
        assertDoesNotThrow { limiter.getSettings() }
    }

    @Test
    fun `set new settings`() {
        val settings = RegistrationLimiterSettingsRequest(99, 99)
        assertDoesNotThrow { limiter.setSettings(settings) }
        val saved = limiter.getSettings()
        assertEquals(
            true,
            saved.deltaCount == settings.deltaCount && saved.deltaTime == settings.deltaTime
        )
    }

    @Test
    fun `test limiter`() {
        val registration = toggleService.findByFeatureName("registration")
        val registrationLimiter = toggleService.findByFeatureName("registrationLimiter")

        toggleService.changeFeatureState(FeatureToggleUpdateRequest("registration", true))
        toggleService.changeFeatureState(FeatureToggleUpdateRequest("registrationLimiter", true))

        var settings = RegistrationLimiterSettingsRequest(1, 100)
        limiter.setSettings(settings)
        for (i in 0..100) {
            limiter.onRegistration()
            Thread.sleep(150)
        }
        assertEquals(true, toggleService.findByFeatureName("registration").enabled)

        settings = RegistrationLimiterSettingsRequest(100, 1)
        limiter.setSettings(settings)
        for (i in 0..100) {
            limiter.onRegistration()
        }
        assertEquals(false, toggleService.findByFeatureName("registration").enabled)

        toggleService.changeFeatureState(FeatureToggleUpdateRequest("registration", registration.enabled))
        toggleService.changeFeatureState(FeatureToggleUpdateRequest("registrationLimiter", registrationLimiter.enabled))

    }
}