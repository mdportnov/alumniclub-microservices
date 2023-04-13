package ru.mephi.alumniclub.app.interceptor

import org.springframework.beans.factory.annotation.Value
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.multipart.MaxUploadSizeExceededException
import org.springframework.web.servlet.NoHandlerFoundException
import ru.mephi.alumniclub.shared.dto.common.ApiError
import ru.mephi.alumniclub.shared.util.response.ResponseManager
import javax.servlet.http.HttpServletRequest


@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
class SomeExceptionHandler(
    @Value("\${spring.servlet.multipart.max-file-size}")
    private val maxFileSize: String
) : ResponseManager() {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(NoHandlerFoundException::class)
    fun handleNoHandlerFoundException(
        request: HttpServletRequest,
        noHandlerFoundException: NoHandlerFoundException
    ): ApiError {
        return ApiError(
            message = i18n("exception.common.noHandlerFound", request.method, request.requestURI),
            status = HttpStatus.BAD_REQUEST,
            debugMessage = noHandlerFoundException.localizedMessage,
            exception = noHandlerFoundException
        )
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MaxUploadSizeExceededException::class)
    fun handleSizeLimitExceededException(): ApiError {
        val message = i18n("exception.media.sizeLimitExceededException", maxFileSize)
        return ApiError(HttpStatus.PAYLOAD_TOO_LARGE, message)
    }
}
