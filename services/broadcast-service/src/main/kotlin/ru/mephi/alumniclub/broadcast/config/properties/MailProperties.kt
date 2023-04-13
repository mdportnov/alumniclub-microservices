package ru.mephi.alumniclub.broadcast.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties(prefix = "spring.mail")
class MailProperties(
    val host: String,
    val port: Int,
    val username: String,
    val password: String,
    val properties: Map<String, String>,
    val fromMail: String = "",
    val defaultFromName: String = ""
)