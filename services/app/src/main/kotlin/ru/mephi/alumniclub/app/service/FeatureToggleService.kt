package ru.mephi.alumniclub.app.service

import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import ru.mephi.alumniclub.app.database.entity.FeatureToggle
import ru.mephi.alumniclub.app.model.dto.FeatureToggleUpdateRequest

interface FeatureToggleService {
    fun isFeatureEnabled(featureName: String): Boolean
    fun changeFeatureState(@RequestBody(required = true) feature: FeatureToggleUpdateRequest): FeatureToggle
    fun listFeatures(): List<FeatureToggle>
    fun findByFeatureName(@PathVariable("featureName") featureName: String): FeatureToggle
}
