package ru.mephi.alumniclub.app.controller.swagger

import io.swagger.v3.oas.annotations.Operation
import org.springdoc.core.annotations.RouterOperation
import org.springdoc.core.annotations.RouterOperations
import org.springframework.web.bind.annotation.RequestMethod
import ru.mephi.alumniclub.app.service.MerchService
import ru.mephi.alumniclub.shared.util.constants.API_VERSION_1

@SwaggerDocumentation
@RouterOperations(
    RouterOperation(
        path = "$API_VERSION_1/merch",
        method = arrayOf(RequestMethod.GET),
        beanClass = MerchService::class,
        beanMethod = "getAllAvailableMerch",
        operation = Operation(
            operationId = "merch",
            description = """Return all available merch."""
        )
    ),
    RouterOperation(
        path = "$API_VERSION_1/admin/merch/{id}/photo",
        method = arrayOf(RequestMethod.POST),
        beanClass = MerchService::class,
        beanMethod = "uploadPhoto",
        operation = Operation(
            operationId = "admin/merch/{id}/photo",
            description = """Upload photo to merch description."""
        )
    ),
    RouterOperation(
        path = "$API_VERSION_1/admin/merch/{id}",
        method = arrayOf(RequestMethod.PUT),
        beanClass = MerchService::class,
        beanMethod = "updateMerch",
        operation = Operation(
            operationId = "PUT admin/merch/{id}",
            description = """Update information about merch."""
        )
    ),
    RouterOperation(
        path = "$API_VERSION_1/admin/merch/{id}",
        method = arrayOf(RequestMethod.DELETE),
        beanClass = MerchService::class,
        beanMethod = "deleteMerch",
        operation = Operation(
            operationId = "DELETE admin/merch/{id}",
            description = """Delete something merch."""
        )
    ),
    RouterOperation(
        path = "$API_VERSION_1/admin/merch",
        method = arrayOf(RequestMethod.GET),
        beanClass = MerchService::class,
        beanMethod = "getAllMerch",
        operation = Operation(
            operationId = "GET admin/merch",
            description = """Return all merch."""
        )
    ),
    RouterOperation(
        path = "$API_VERSION_1/admin/merch",
        method = arrayOf(RequestMethod.POST),
        beanClass = MerchService::class,
        beanMethod = "createMerch",
        operation = Operation(
            operationId = "POST admin/merch",
            description = """Create new merch."""
        )
    ),
)
annotation class MerchRoutingDoc