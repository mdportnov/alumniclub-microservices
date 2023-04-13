package ru.mephi.alumniclub.app.controller.swagger

import io.swagger.v3.oas.annotations.Operation
import org.springdoc.core.annotations.RouterOperation
import org.springdoc.core.annotations.RouterOperations
import org.springframework.web.bind.annotation.RequestMethod
import ru.mephi.alumniclub.app.service.FormService
import ru.mephi.alumniclub.shared.util.constants.API_VERSION_1


@SwaggerDocumentation
@RouterOperations(
    RouterOperation(
        path = "$API_VERSION_1/form",
        method = arrayOf(RequestMethod.POST),
        beanClass = FormService::class,
        beanMethod = "saveForm",
        operation = Operation(
            operationId = "POST form",
            description = """Creates new form."""
        )
    ),
    RouterOperation(
        path = "$API_VERSION_1/form",
        method = arrayOf(RequestMethod.GET),
        beanClass = FormService::class,
        beanMethod = "getFormByTypeAndUserId",
        operation = Operation(
            operationId = "GET form",
            description = """Returns one of entered user forms."""
        )
    ),
    RouterOperation(
        path = "$API_VERSION_1/form/self",
        method = arrayOf(RequestMethod.GET),
        beanClass = FormService::class,
        beanMethod = "getFormByTypeAndUserId",
        operation = Operation(
            operationId = "form/self",
            description = """Returns one of self forms"""
        )
    ),
    RouterOperation(
        path = "$API_VERSION_1/admin/form/user",
        method = arrayOf(RequestMethod.GET),
        beanClass = FormService::class,
        beanMethod = "listAllUserForms",
        operation = Operation(
            operationId = "admin/form/user",
            description = """Returns all user forms."""
        )
    ),
    RouterOperation(
        path = "$API_VERSION_1/admin/form/status",
        method = arrayOf(RequestMethod.POST),
        beanClass = FormService::class,
        beanMethod = "changeStatus",
        operation = Operation(
            operationId = "admin/form/status",
            description = """Changes status of entered form."""
        )
    ),
    RouterOperation(
        path = "$API_VERSION_1/admin/form",
        method = arrayOf(RequestMethod.GET),
        beanClass = FormService::class,
        beanMethod = "listByType",
        operation = Operation(
            operationId = "admin/form",
            description = """Returns all forms of the desired type from all forms of all users."""
        )
    ),
)
annotation class FormRoutingDoc