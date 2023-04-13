package ru.mephi.alumniclub.app.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties(prefix = "swagger")
class SwaggerProperties(
    val swaggerLogin: String,
    val swaggerPassword: String,
)