package ru.mephi.alumniclub.app.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties(prefix = "urls")
class ServerUrlsProperties(
    val baseUrl: String
) {
    val fullBaseUrl = "https://$baseUrl"
    val homePage = "$fullBaseUrl/home"
}