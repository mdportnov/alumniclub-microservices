package ru.mephi.alumniclub.app.service.impl

import org.springframework.stereotype.Service
import ru.mephi.alumniclub.app.config.properties.FeatureToggleProperties
import ru.mephi.alumniclub.app.database.entity.FeatureToggle
import ru.mephi.alumniclub.app.database.repository.FeatureToggleDao
import ru.mephi.alumniclub.app.model.dto.FeatureToggleUpdateRequest
import ru.mephi.alumniclub.app.model.exception.common.ResourceNotFoundException
import ru.mephi.alumniclub.app.service.FeatureToggleService
import ru.mephi.alumniclub.shared.util.response.ResponseManager

@Service
class FeatureToggleServiceImpl(
    private val dao: FeatureToggleDao,
    private val featureToggleProperties: FeatureToggleProperties
) : ResponseManager(), FeatureToggleService {

    init {
        featureToggleProperties.featureState.forEach {
            if (!dao.existsByFeatureName(it.key))
                dao.save(
                    FeatureToggle(
                        it.key,
                        featureToggleProperties.featureDescription[it.key]!!,
                        it.value
                    )
                )
        }
    }

    override fun isFeatureEnabled(featureName: String): Boolean {
        val feature = dao.findByFeatureName(featureName)
            .orElseThrow { ResourceNotFoundException(FeatureToggle::class.java) }
        return feature.enabled
    }

    override fun changeFeatureState(feature: FeatureToggleUpdateRequest): FeatureToggle {
        if (dao.existsByFeatureName(feature.featureName)) {
            dao.updateFeatureFlag(feature.featureName, feature.enabled)
            return dao.findByFeatureName(feature.featureName).get()
        } else throw ResourceNotFoundException(FeatureToggle::class.java)
    }

    override fun listFeatures(): List<FeatureToggle> {
        return dao.findAll().toList()
    }

    override fun findByFeatureName(featureName: String): FeatureToggle {
        return dao.findByFeatureName(featureName).orElseThrow { ResourceNotFoundException(FeatureToggle::class.java) }
    }
}
