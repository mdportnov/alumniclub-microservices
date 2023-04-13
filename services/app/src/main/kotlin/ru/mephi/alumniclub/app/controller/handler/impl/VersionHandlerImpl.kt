package ru.mephi.alumniclub.app.controller.handler.impl

import org.springframework.stereotype.Service
import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse
import ru.mephi.alumniclub.app.controller.handler.VersionHandler
import ru.mephi.alumniclub.app.service.AppVersionService
import ru.mephi.alumniclub.shared.util.extension.paramOrThrow
import ru.mephi.alumniclub.shared.util.extension.toOkBody

@Service
class VersionHandlerImpl(
    private val service: AppVersionService,
) : VersionHandler {

    override fun isUpdateRequired(request: ServerRequest): ServerResponse {
        val version = request.paramOrThrow<String>("version")
        val response = service.isUpdateRequired(version)
        return response.toOkBody()
    }

    override fun getMinSupportedAPIVersion(request: ServerRequest): ServerResponse {
        val response = service.getMinSupportedAPIVersion()
        return response.toOkBody()
    }

    override fun getBackendVersion(request: ServerRequest): ServerResponse {
        val response = service.getBackendVersion()
        return response.toOkBody()
    }
}