package ru.mephi.alumniclub.app.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import ru.mephi.alumniclub.app.util.version.ApiVersion

@ConstructorBinding
@ConfigurationProperties(prefix = "api.versions")
class ApiVersionProperties(
    val minSupportedClientVersion: String = "0.0.0"
) {
    fun convertToApiVersion() = ApiVersion(minSupportedClientVersion)
}

