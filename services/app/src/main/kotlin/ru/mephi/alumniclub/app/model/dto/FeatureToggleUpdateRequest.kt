package ru.mephi.alumniclub.app.model.dto

import javax.validation.constraints.Size

class FeatureToggleUpdateRequest(
    @field:Size(max = 60)
    val featureName: String,
    val enabled: Boolean
)