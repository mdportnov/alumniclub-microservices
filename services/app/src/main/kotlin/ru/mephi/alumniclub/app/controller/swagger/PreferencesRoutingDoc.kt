package ru.mephi.alumniclub.app.controller.swagger

import io.swagger.v3.oas.annotations.Operation
import org.springdoc.core.annotations.RouterOperation
import org.springdoc.core.annotations.RouterOperations
import org.springframework.web.bind.annotation.RequestMethod
import ru.mephi.alumniclub.app.service.UserPreferencesService
import ru.mephi.alumniclub.shared.util.constants.API_VERSION_1


@SwaggerDocumentation
@RouterOperations(
    RouterOperation(
        path = "$API_VERSION_1/preferences",
        method = arrayOf(RequestMethod.GET),
        beanClass = UserPreferencesService::class,
        beanMethod = "getPreferencesById",
        operation = Operation(
            operationId = "GET preferences",
            description = """Returns self user preferences."""
        )
    ),
    RouterOperation(
        path = "$API_VERSION_1/preferences",
        method = arrayOf(RequestMethod.PUT),
        beanClass = UserPreferencesService::class,
        beanMethod = "updatePreferences",
        operation = Operation(
            operationId = "PUT preferences",
            description = """Updates self user preferences."""
        )
    ),
    RouterOperation(
        path = "$API_VERSION_1/admin/preferences/{id}",
        method = arrayOf(RequestMethod.GET),
        beanClass = UserPreferencesService::class,
        beanMethod = "getPreferencesById",
        operation = Operation(
            operationId = "admin/preferences/{id}",
            description = """Returns preferences of entered user."""
        )
    ),
)
annotation class PreferencesRoutingDoc