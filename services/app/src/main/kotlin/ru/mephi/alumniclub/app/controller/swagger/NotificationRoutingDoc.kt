package ru.mephi.alumniclub.app.controller.swagger

import io.swagger.v3.oas.annotations.Operation
import org.springdoc.core.annotations.RouterOperation
import org.springdoc.core.annotations.RouterOperations
import org.springframework.web.bind.annotation.RequestMethod
import ru.mephi.alumniclub.app.service.NotificationService
import ru.mephi.alumniclub.shared.util.constants.API_VERSION_1

@SwaggerDocumentation
@RouterOperations(
    RouterOperation(
        path = "$API_VERSION_1/notification",
        method = arrayOf(RequestMethod.GET),
        beanClass = NotificationService::class,
        beanMethod = "list",
        operation = Operation(
            operationId = "GET notifications",
            description = """Returns to user a list of short information about notifications
                    owned by this user with cursor pagination."""
        )
    ),
    RouterOperation(
        path = "$API_VERSION_1/notifications/{id}",
        method = arrayOf(RequestMethod.GET),
        beanClass = NotificationService::class,
        beanMethod = "findByIdForUser",
        operation = Operation(
            operationId = "GET notifications/{id}",
            description = """Returns short information about notification by id."""
        )
    ),
    RouterOperation(
        path = "$API_VERSION_1/notifications/{id}",
        method = arrayOf(RequestMethod.DELETE),
        beanClass = NotificationService::class,
        beanMethod = "deleteForUser",
        operation = Operation(
            operationId = "DELETE notifications/{id}",
            description = """Removes the notification from the user who made this request."""
        )
    ),
    RouterOperation(
        path = "$API_VERSION_1/admin/notifications",
        method = arrayOf(RequestMethod.GET),
        beanClass = NotificationService::class,
        beanMethod = "list",
        operation = Operation(
            operationId = "GET admin/notifications",
            description = """Returns a list of short information about notifications
                      for admin with page pagination."""
        )
    ),
    RouterOperation(
        path = "$API_VERSION_1/admin/notifications/{id}",
        method = arrayOf(RequestMethod.DELETE),
        beanClass = NotificationService::class,
        beanMethod = "delete",
        operation = Operation(
            operationId = "DELETE admin/notifications/{id}",
            description = """Removes the notification to all users."""
        )
    ),
    RouterOperation(
        path = "$API_VERSION_1/admin/notifications/{id}",
        method = arrayOf(RequestMethod.GET),
        beanClass = NotificationService::class,
        beanMethod = "findByIdForAdmin",
        operation = Operation(
            operationId = "GET admin/notifications/{id}",
            description = """Returns the notification."""
        )
    ),
    RouterOperation(
        path = "$API_VERSION_1/admin/notifications",
        method = arrayOf(RequestMethod.POST),
        beanClass = NotificationService::class,
        beanMethod = "createNew",
        operation = Operation(
            operationId = "admin/notifications",
            description = """Create new notification."""
        )
    )
)
annotation class NotificationRoutingDoc