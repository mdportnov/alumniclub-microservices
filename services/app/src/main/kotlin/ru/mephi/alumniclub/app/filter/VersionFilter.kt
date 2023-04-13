package ru.mephi.alumniclub.app.filter

import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import ru.mephi.alumniclub.app.config.properties.ApiVersionProperties
import ru.mephi.alumniclub.app.interceptor.ExceptionResolver
import ru.mephi.alumniclub.app.util.version.ApiVersion
import ru.mephi.alumniclub.shared.dto.common.ApiError
import ru.mephi.alumniclub.shared.util.extension.containsAnyPath
import ru.mephi.alumniclub.shared.util.response.ResponseHandler
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class VersionFilter(
    private val apiVersionProperties: ApiVersionProperties,
    private val exceptionResolver: ExceptionResolver,
    private val responseHandler: ResponseHandler
) : OncePerRequestFilter() {
    val skippedRoutesForVersionUpdateRequired = arrayOf("/logout")

    override fun doFilterInternal(
        request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain
    ) {
        response.addHeader("X-API-Version", apiVersionProperties.minSupportedClientVersion)

        try {
            val clientVersionHeader = request.getHeader("X-Client-Version")

            if (!clientVersionHeader.isNullOrBlank()) checkClientVersion(ApiVersion(clientVersionHeader), request)
            filterChain.doFilter(request, response)
        } catch (exception: ApiError) {
            exceptionResolver.resolveException(request, response, exception)
        }
    }

    private fun checkClientVersion(clientVersion: ApiVersion, request: HttpServletRequest) {
        val backendClientTargetAPI = apiVersionProperties.convertToApiVersion()
        val minSupportedVersion = ApiVersion(major = backendClientTargetAPI.MAJOR, minor = backendClientTargetAPI.MINOR)

        if (request.requestURI.containsAnyPath(*skippedRoutesForVersionUpdateRequired)) return

        if (clientVersion < minSupportedVersion) throw ApiError(
            HttpStatus.UPGRADE_REQUIRED, responseHandler.i18n(
                "exception.common.notSupportedClientVersion",
                request.requestURI,
                "$clientVersion",
                "$minSupportedVersion"
            )
        )
    }
}

