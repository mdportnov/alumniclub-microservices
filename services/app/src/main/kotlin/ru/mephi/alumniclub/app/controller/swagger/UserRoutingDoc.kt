package ru.mephi.alumniclub.app.controller.swagger

import io.swagger.v3.oas.annotations.Operation
import org.springdoc.core.annotations.RouterOperation
import org.springdoc.core.annotations.RouterOperations
import org.springframework.web.bind.annotation.RequestMethod
import ru.mephi.alumniclub.app.service.UserService
import ru.mephi.alumniclub.shared.util.constants.API_VERSION_1


@SwaggerDocumentation
@RouterOperations(
    RouterOperation(
        path = "$API_VERSION_1/user/{id}",
        method = arrayOf(RequestMethod.GET),
        beanClass = UserService::class,
        beanMethod = "findById",
        operation = Operation(
            operationId = "GET user/{id}",
            description = """Get user information by id, masked fields will be null'ed."""
        )
    ),
    RouterOperation(
        path = "$API_VERSION_1/user/me",
        method = arrayOf(RequestMethod.GET),
        beanClass = UserService::class,
        beanMethod = "findById",
        operation = Operation(
            operationId = "GET user/me",
            description = """Get self user information without masking."""
        )
    ),
    RouterOperation(
        path = "$API_VERSION_1/user/me",
        method = arrayOf(RequestMethod.PUT),
        beanClass = UserService::class,
        beanMethod = "update",
        operation = Operation(
            operationId = "PUT user/me",
            description = """Update self user information."""
        )
    ),
    RouterOperation(
        path = "$API_VERSION_1/user/me/photo",
        method = arrayOf(RequestMethod.POST),
        beanClass = UserService::class,
        beanMethod = "uploadPhoto",
        operation = Operation(
            operationId = "POST user/me/photo",
            description = """Upload a photo for self account."""
        )
    ),
    RouterOperation(
        path = "$API_VERSION_1/user/me",
        method = arrayOf(RequestMethod.DELETE),
        beanClass = UserService::class,
        beanMethod = "delete",
        operation = Operation(
            operationId = "DELETE user/me",
            description = """Delete self account."""
        )
    ),
    RouterOperation(
        path = "$API_VERSION_1/user/me/visibility",
        method = arrayOf(RequestMethod.GET),
        beanClass = UserService::class,
        beanMethod = "getVisibility",
        operation = Operation(
            operationId = "GET user/me/visibility",
            description = """Get visibility of self user information fields."""
        )
    ),
    RouterOperation(
        path = "$API_VERSION_1/user/me/visibility",
        method = arrayOf(RequestMethod.PUT),
        beanClass = UserService::class,
        beanMethod = "updateVisibility",
        operation = Operation(
            operationId = "PUT user/me/visibility",
            description = """Update visibility of self user information fields."""
        )
    ),

    RouterOperation(
        path = "$API_VERSION_1/admin/user",
        method = arrayOf(RequestMethod.GET),
        beanClass = UserService::class,
        beanMethod = "list",
        operation = Operation(
            operationId = "GET admin/user",
            description = """List users, ignore masked fields."""
        )
    ),
    RouterOperation(
        path = "$API_VERSION_1/admin/user/{id}",
        method = arrayOf(RequestMethod.GET),
        beanClass = UserService::class,
        beanMethod = "findById",
        operation = Operation(
            operationId = "GET admin/user/{id}",
            description = """Get user information by id, ignore masked fields."""
        )
    ),
    RouterOperation(
        path = "$API_VERSION_1/admin/user/{id}",
        method = arrayOf(RequestMethod.DELETE),
        beanClass = UserService::class,
        beanMethod = "delete",
        operation = Operation(
            operationId = "DELETE admin/user/{id}",
            description = """Delete user, admin can't delete self with this request."""
        )
    ),
    RouterOperation(
        path = "$API_VERSION_1/admin/user/{id}/ban",
        method = arrayOf(RequestMethod.PUT),
        beanClass = UserService::class,
        beanMethod = "setBanStatus",
        operation = Operation(
            operationId = "PUT admin/user/{id}/ban",
            description = """Sets the ban status for the user."""
        )
    ),
    RouterOperation(
        path = "$API_VERSION_1/admin/user/{id}/communities",
        method = arrayOf(RequestMethod.GET),
        beanClass = UserService::class,
        beanMethod = "listUserCommunities",
        operation = Operation(
            operationId = "GET admin/user/{id}/communities",
            description = """List communities in which the user participates."""
        )
    ),
)
annotation class UserRoutingDoc