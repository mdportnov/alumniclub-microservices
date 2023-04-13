package ru.mephi.alumniclub.app.controller.swagger

import io.swagger.v3.oas.annotations.Operation
import org.springdoc.core.annotations.RouterOperation
import org.springdoc.core.annotations.RouterOperations
import org.springframework.web.bind.annotation.RequestMethod
import ru.mephi.alumniclub.app.service.FeatureToggleService
import ru.mephi.alumniclub.shared.util.constants.API_VERSION_1

@SwaggerDocumentation
@RouterOperations(
    RouterOperation(
        path = "$API_VERSION_1/admin/feature/{featureName}",
        method = arrayOf(RequestMethod.GET),
        beanClass = FeatureToggleService::class,
        beanMethod = "findByFeatureName",
        operation = Operation(
            operationId = "GET admin/feature",
            description = """Returns now value of entered toggle feature."""
        )
    ),
    RouterOperation(
        path = "$API_VERSION_1/admin/feature",
        method = arrayOf(RequestMethod.PUT),
        beanClass = FeatureToggleService::class,
        beanMethod = "changeFeatureState",
        operation = Operation(
            operationId = "POST admin/feature",
            description = """Changes toggle feature value."""
        )
    ),
    RouterOperation(
        path = "$API_VERSION_1/admin/feature",
        method = arrayOf(RequestMethod.GET),
        beanClass = FeatureToggleService::class,
        beanMethod = "listFeatures",
        operation = Operation(
            operationId = "admin/feature",
            description = """Returns information about all toggle features."""
        )
    ),
)
annotation class FeatureToggleRoutingDoc