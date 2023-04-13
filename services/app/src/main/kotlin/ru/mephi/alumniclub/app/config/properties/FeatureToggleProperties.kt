package ru.mephi.alumniclub.app.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties(prefix = "feature-toggle")
class FeatureToggleProperties(
    val featureState: Map<String, Boolean> = mapOf(),
    val featureDescription: Map<String, String> = mapOf()
)