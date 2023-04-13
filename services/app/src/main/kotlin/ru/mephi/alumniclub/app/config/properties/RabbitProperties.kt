package ru.mephi.alumniclub.app.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties(prefix = "rabbitmq")
class RabbitProperties(
    val host: String,
    val port: Int,
    val username: String,
    val password: String,
    val replyTimeout: Long
)