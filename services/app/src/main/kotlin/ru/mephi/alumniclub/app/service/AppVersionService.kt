package ru.mephi.alumniclub.app.service

import ru.mephi.alumniclub.app.model.dto.version.UpdateRequiredResponse
import ru.mephi.alumniclub.app.model.dto.version.VersionResponse

interface AppVersionService {
    fun isUpdateRequired(clientVersion: String): UpdateRequiredResponse
    fun getBackendVersion(): VersionResponse
    fun getMinSupportedAPIVersion(): VersionResponse
}