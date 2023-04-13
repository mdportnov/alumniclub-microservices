package ru.mephi.alumniclub.app.database.repository

import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import ru.mephi.alumniclub.app.database.entity.FeatureToggle
import ru.mephi.alumniclub.shared.database.repository.AbstractRepository
import java.util.*

@Repository
interface FeatureToggleDao : AbstractRepository<FeatureToggle> {
    fun findByFeatureName(name: String): Optional<FeatureToggle>

    fun existsByFeatureName(name: String): Boolean

    @Modifying
    @Transactional
    @Query("update FeatureToggle f set f.enabled = :enabled where f.featureName = :name")
    fun updateFeatureFlag(name: String, enabled: Boolean)
}
