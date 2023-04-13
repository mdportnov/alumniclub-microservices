package ru.mephi.alumniclub.app.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties(prefix = "actuator")
class ActuatorAuthProperties(
    val login: String,
    val password: String,
)