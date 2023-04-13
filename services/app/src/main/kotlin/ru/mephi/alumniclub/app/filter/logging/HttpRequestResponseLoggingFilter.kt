package ru.mephi.alumniclub.app.filter.logging

import org.springframework.http.server.ServletServerHttpRequest
import org.springframework.util.StringUtils
import org.springframework.web.filter.AbstractRequestLoggingFilter
import org.springframework.web.util.ContentCachingRequestWrapper
import ru.mephi.alumniclub.shared.util.extension.containsAnyPath
import ru.mephi.alumniclub.shared.util.extension.isMultipart
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * This is basically the same as
 * [org.springframework.web.filter.CommonsRequestLoggingFilter] with
 * additional features: 1) print in info level, 2) print request method, 3)
 * print response body.
 *
 * @see [This stackoverflow link] (https://stackoverflow.com/questions/8933054/how-to-read-and-copy-the-http-servlet-response-output-stream-content-for-logging)
 * @see [This github link] (https://github.com/yuhuachang/java-spring-boot-samples/tree/master/spring-rest-logging)
 */
class HttpRequestResponseLoggingFilter : AbstractRequestLoggingFilter() {
    init {
        isIncludeClientInfo = true
        isIncludeQueryString = true
        isIncludeHeaders = true
        isIncludePayload = true
        maxPayloadLength = 50000
    }

    private fun shouldSkip(request: HttpServletRequest): Boolean {
        return request.requestURI.containsAnyPath("/api/actuator", "/api/v3/api-docs")
    }

    override fun shouldLog(request: HttpServletRequest): Boolean {
        return logger.isInfoEnabled && !shouldSkip(request)
    }

    private fun registrationMask(message: String): String {
        return message.replace(
            "\"password\":\\s*\"[^\"]*\"".toRegex(), "\"password\": \"*****\""
        )
    }

    override fun doFilterInternal(
        request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain
    ) {
        val isFirstRequest = !isAsyncDispatch(request)  // check if this is the first request

        var requestToLog = request

        if (!request.isMultipart() && isIncludePayload && isFirstRequest && request !is ContentCachingRequestWrapper) {
            requestToLog = HttpRequestWrapper(request)
        }

        val shouldLog = shouldLog(requestToLog)
        if (shouldLog && isFirstRequest) {
            var logMessage = createMessage(requestToLog, "API Request [", "]")
            if (requestToLog.requestURI.containsAnyPath("/auth/register", "/auth/login")) logMessage =
                registrationMask(logMessage)

            logger.info(logMessage)
        }

        try {
            filterChain.doFilter(requestToLog, response) // requestToLog (not simple request!),
        } finally {
            if (shouldLog && !isAsyncStarted(requestToLog)) {
                logger.info(createResponseMessage(response))
            }
        }
    }

    override fun createMessage(request: HttpServletRequest, prefix: String, suffix: String): String {
        return StringBuilder().run {
            append(prefix)
            append("method=").append(request.method)
            append(";uri=").append(request.requestURI)
            if (isIncludeQueryString) {
                val queryString = request.queryString
                if (queryString != null) {
                    append('?').append(queryString)
                }
            }
            if (isIncludeClientInfo) {
                val client = request.remoteAddr
                if (StringUtils.hasLength(client)) {
                    append(";client=").append(client)
                }
                val session = request.getSession(false)
                if (session != null) {
                    append(";session=").append(session.id)
                }
                val user = request.remoteUser
                if (user != null) {
                    append(";user=").append(user)
                }
            }
            if (isIncludeHeaders) {
                append(";headers=").append(ServletServerHttpRequest(request).headers)
            }
            if (isIncludePayload) {
                if (request.isMultipart()) {
                    append(";payload=").append("some multipart/form-data content")
                } else
                    append(";payload=").append((request as HttpRequestWrapper).getBody())
            }
            append(suffix)
            toString()
        }
    }

    private fun createResponseMessage(
        response: HttpServletResponse,
        prefix: String = "API Response [",
        suffix: String = "]"
    ): String {
        return StringBuilder().run {
            append(prefix)
            append("statusCode=").append(response.status)
            append(";contentType=").append(response.contentType ?: "")
            append(suffix)
            toString()
        }
    }

    override fun beforeRequest(request: HttpServletRequest, message: String) {
        return
    }

    override fun afterRequest(request: HttpServletRequest, message: String) {
        return
    }
}