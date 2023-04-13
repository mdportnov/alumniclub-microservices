package ru.mephi.alumniclub.app.controller.swagger

import io.swagger.v3.oas.annotations.Operation
import org.springdoc.core.annotations.RouterOperation
import org.springdoc.core.annotations.RouterOperations
import org.springframework.web.bind.annotation.RequestMethod
import ru.mephi.alumniclub.app.service.RegistrationLimiterService
import ru.mephi.alumniclub.shared.util.constants.API_VERSION_1

@SwaggerDocumentation
@RouterOperations(
    RouterOperation(
        path = "$API_VERSION_1/admin/settings/registration-limiter",
        method = arrayOf(RequestMethod.PUT),
        beanClass = RegistrationLimiterService::class,
        beanMethod = "setSettings",
        operation = Operation(
            operationId = "PUT admin/settings/registration-limiter",
            description = """Sets new settings to registration limiter."""
        )
    ),
    RouterOperation(
        path = "$API_VERSION_1/admin/settings/registration-limiter",
        method = arrayOf(RequestMethod.GET),
        beanClass = RegistrationLimiterService::class,
        beanMethod = "getSettings",
        operation = Operation(
            operationId = "GET admin/settings/registration-limiter",
            description = """Returns settings of registration limiter."""
        )
    )
)
annotation class RegistrationLimiterRoutingDoc
