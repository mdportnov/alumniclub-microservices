package ru.mephi.alumniclub.shared.util.response

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.module.kotlin.MissingKotlinParameterException
import org.apache.tomcat.util.http.fileupload.impl.InvalidContentTypeException
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.context.support.ResourceBundleMessageSource
import org.springframework.dao.InvalidDataAccessApiUsageException
import org.springframework.data.mapping.PropertyReferenceException
import org.springframework.http.HttpStatus
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.stereotype.Component
import org.springframework.web.servlet.function.ServerResponse
import ru.mephi.alumniclub.shared.dto.common.ApiError
import ru.mephi.alumniclub.shared.dto.common.ValidationError
import java.lang.reflect.UndeclaredThrowableException

@Component
class ResponseHandler(
    private val messageSource: ResourceBundleMessageSource
) {
    fun i18n(label: String, vararg args: String?): String {
        val locale = LocaleContextHolder.getLocale()
        return messageSource.getMessage(label, args, locale)
    }

    fun handleError(throwable: Throwable): ServerResponse {
        val apiError = handleExceptionToApiError(throwable as Exception)
        return ServerResponse.status(apiError.status).body(apiError)
    }

    fun handleExceptionToApiError(exception: Exception): ApiError {
        return when (exception) {
            is ApiError -> exception
            is UndeclaredThrowableException -> {
                val apiError = when (exception.cause) {
                    is ApiError -> exception.cause as ApiError
                    else -> ApiError(
                        status = HttpStatus.INTERNAL_SERVER_ERROR,
                        message = i18n("exception.common.internalServerError"),
                        exception = exception.cause,
                        debugMessage = exception.toString(),
                    )
                }
                apiError
            }

            is InvalidContentTypeException -> ApiError(
                status = HttpStatus.NOT_ACCEPTABLE,
                message = exception.localizedMessage,
                exception = exception,
            )

            is MissingKotlinParameterException, is HttpMessageNotReadableException, is IllegalArgumentException -> {
                ApiError(
                    status = HttpStatus.UNPROCESSABLE_ENTITY,
                    message = i18n("exception.common.cannotProcessData"),
                    debugMessage = exception.message ?: exception::class.qualifiedName.toString(),
                    exception = exception
                )
            }

            is JsonProcessingException -> {
                val field =
                    Regex("\\[\"\\w+\"]").find(exception.localizedMessage)?.value?.replace(Regex("[\\[\\]\"]"), "")
                        ?: ""
                ApiError(
                    status = HttpStatus.UNPROCESSABLE_ENTITY,
                    message = i18n("exception.common.cannotProcessData"),
                    validationErrors = listOf(
                        ValidationError("Request", field, "Invalid field")
                    )
                )
            }

            is InvalidDataAccessApiUsageException, is PropertyReferenceException -> ApiError(
                status = HttpStatus.BAD_REQUEST,
                message = exception.localizedMessage,
                exception = exception,
            )

            else -> ApiError(
                status = HttpStatus.INTERNAL_SERVER_ERROR,
                message = i18n("exception.common.internalServerError"),
                debugMessage = exception.message ?: exception::class.qualifiedName.toString(),
                exception = exception
            )
        }
    }
}
