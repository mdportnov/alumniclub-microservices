package ru.mephi.alumniclub.app.service.impl

import org.springframework.boot.info.BuildProperties
import org.springframework.stereotype.Service
import ru.mephi.alumniclub.app.config.properties.ApiVersionProperties
import ru.mephi.alumniclub.app.model.dto.version.UpdateRequiredResponse
import ru.mephi.alumniclub.app.model.dto.version.VersionResponse
import ru.mephi.alumniclub.app.service.AppVersionService
import ru.mephi.alumniclub.app.util.version.ApiVersion

@Service
class AppVersionServiceImpl(
    private val apiVersionProperties: ApiVersionProperties,
    private val buildProperties: BuildProperties
) : AppVersionService {
    override fun isUpdateRequired(clientVersion: String): UpdateRequiredResponse {
        val backendClientTargetAPI = apiVersionProperties.convertToApiVersion()
        val minSupportedVersion = ApiVersion(major = backendClientTargetAPI.MAJOR, minor = backendClientTargetAPI.MINOR)
        val required = ApiVersion(clientVersion) < minSupportedVersion
        return UpdateRequiredResponse(required, minSupportedVersion.toString())
    }

    override fun getBackendVersion(): VersionResponse {
        return VersionResponse(buildProperties.version)
    }

    override fun getMinSupportedAPIVersion(): VersionResponse {
        return VersionResponse(apiVersionProperties.minSupportedClientVersion)
    }
}