package ru.mephi.alumniclub.app.interceptor

import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.http.entity.ContentType
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import ru.mephi.alumniclub.shared.dto.common.ApiError
import ru.mephi.alumniclub.shared.util.response.ResponseHandler
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@RestControllerAdvice
@Order(Ordered.LOWEST_PRECEDENCE)
class ExceptionResolver(
    private val responseHandler: ResponseHandler
) {
    /**
     * Method used to be invoked in interceptors for
     * - catch-blocks in common try-catches
     * - interceptor-specific logic with throwing ApiError
     * @author mdportnov
     */
    @ExceptionHandler(ApiError::class)
    fun resolveException(
        request: HttpServletRequest, response: HttpServletResponse, exception: Exception
    ) {
        val exceptionToResponse = responseHandler.handleExceptionToApiError(exception)
        val objectMapper = ObjectMapper().findAndRegisterModules()
        response.contentType = ContentType.APPLICATION_JSON.mimeType
        response.status = exceptionToResponse.status.value()
        response.characterEncoding = "UTF-8"
        response.writer.write(objectMapper.writeValueAsString(exceptionToResponse))
    }
}
