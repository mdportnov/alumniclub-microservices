package ru.mephi.alumniclub.app.database.entity

import ru.mephi.alumniclub.shared.database.entity.AbstractEntity
import ru.mephi.alumniclub.shared.util.constants.smallLength
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(name = "FeatureToggle")
class FeatureToggle(
    @Column(name = "feature", unique = true, length = smallLength)
    val featureName: String,
    @Column(name = "featureDescription", length = smallLength)
    val featureDescription: String,
    @Column(name = "enabled")
    val enabled: Boolean,
) : AbstractEntity()
