package ru.mephi.alumniclub.shared.util

import org.springframework.web.servlet.function.RouterFunctionDsl
import org.springframework.web.servlet.function.ServerResponse
import org.springframework.web.servlet.function.router
import ru.mephi.alumniclub.shared.dto.common.ApiError
import ru.mephi.alumniclub.shared.util.response.ResponseHandler

/**
 * Handles directly thrown exceptions ([ApiError]) and its descendants from handlers/services:
 * ~~~
 * onError<ApiError> { error, _ -> (error as ApiError).asResponse() }
 * ~~~
 *
 * Handles unexpected exceptions from handlers/services:
 * ~~~
 * onError<Throwable> { error, _ -> errorHandler.handleError(error) }
 * ~~~
 */
fun modernRouter(responseHandler: ResponseHandler, routes: (RouterFunctionDsl.() -> Unit)) = router {
    routes()
    onError<ApiError> { error, _ ->
        error as ApiError
        error.message = responseHandler.i18n(error.message)
        ServerResponse.status(error.status).body(error)
    }
    onError<Throwable> { error, _ -> responseHandler.handleError(error) }
}
