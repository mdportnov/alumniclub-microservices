package ru.mephi.alumniclub.app.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties(prefix = "redis")
class RedisProperties(
    val cacheTTL: Long = 86400
)