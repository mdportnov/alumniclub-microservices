package ru.mephi.alumniclub.app.controller.swagger

import io.swagger.v3.oas.annotations.Operation
import org.springdoc.core.annotations.RouterOperation
import org.springdoc.core.annotations.RouterOperations
import org.springframework.web.bind.annotation.RequestMethod
import ru.mephi.alumniclub.app.service.CommunityService
import ru.mephi.alumniclub.shared.util.constants.API_VERSION_1


@SwaggerDocumentation
@RouterOperations(
    RouterOperation(
        path = "$API_VERSION_1/community/my",
        method = arrayOf(RequestMethod.GET),
        beanClass = CommunityService::class,
        beanMethod = "listByUser",
        operation = Operation(
            operationId = "GET community/my",
            description = """List of communities in which the user participates."""
        )
    ),
    RouterOperation(
        path = "$API_VERSION_1/community",
        method = arrayOf(RequestMethod.GET),
        beanClass = CommunityService::class,
        beanMethod = "GET listByUserNot",
        operation = Operation(
            operationId = "GET community",
            description = """List communities in which the user participates."""
        )
    ),
    RouterOperation(
        path = "$API_VERSION_1/community/{id}",
        method = arrayOf(RequestMethod.GET),
        beanClass = CommunityService::class,
        beanMethod = "findById",
        operation = Operation(
            operationId = "GET community/{id}",
            description = """Get community information."""
        )
    ),
    RouterOperation(
        path = "$API_VERSION_1/community/{id}/members",
        method = arrayOf(RequestMethod.GET),
        beanClass = CommunityService::class,
        beanMethod = "listMembersForUser",
        operation = Operation(
            operationId = "GET community/{id}/members",
            description = """List community members."""
        )
    ),
    RouterOperation(
        path = "$API_VERSION_1/community/{id}/join",
        method = arrayOf(RequestMethod.POST),
        beanClass = CommunityService::class,
        beanMethod = "leave",
        operation = Operation(
            operationId = "POST community/{id}/leave",
            description = """Join self to community."""
        )
    ),
    RouterOperation(
        path = "$API_VERSION_1/community/{id}/leave",
        method = arrayOf(RequestMethod.POST),
        beanClass = CommunityService::class,
        beanMethod = "leave",
        operation = Operation(
            operationId = "POST community/{id}/leave",
            description = """Leave from community."""
        )
    ),
    RouterOperation(
        path = "$API_VERSION_1/admin/community",
        method = arrayOf(RequestMethod.GET),
        beanClass = CommunityService::class,
        beanMethod = "list",
        operation = Operation(
            operationId = "GET admin/community",
            description = """List communities, include hidden communities."""
        )
    ),
    RouterOperation(
        path = "$API_VERSION_1/admin/community",
        method = arrayOf(RequestMethod.POST),
        beanClass = CommunityService::class,
        beanMethod = "create",
        operation = Operation(
            operationId = "POST admin/community",
            description = """Create new community."""
        )
    ),
    RouterOperation(
        path = "$API_VERSION_1/admin/community/{id}",
        method = arrayOf(RequestMethod.GET),
        beanClass = CommunityService::class,
        beanMethod = "findById",
        operation = Operation(
            operationId = "GET admin/community/{id}",
            description = """Get community information."""
        )
    ),
    RouterOperation(
        path = "$API_VERSION_1/admin/community/{id}",
        method = arrayOf(RequestMethod.PUT),
        beanClass = CommunityService::class,
        beanMethod = "update",
        operation = Operation(
            operationId = "PUT admin/community/{id}",
            description = """Update community information."""
        )
    ),
    RouterOperation(
        path = "$API_VERSION_1/admin/community/{id}",
        method = arrayOf(RequestMethod.DELETE),
        beanClass = CommunityService::class,
        beanMethod = "delete",
        operation = Operation(
            operationId = "DELETE admin/community/{id}",
            description = """Delete community."""
        )
    ),
    RouterOperation(
        path = "$API_VERSION_1/admin/community/{id}/photo",
        method = arrayOf(RequestMethod.POST),
        beanClass = CommunityService::class,
        beanMethod = "uploadPhoto",
        operation = Operation(
            operationId = "POST admin/community/{id}/photo",
            description = """Uploads photo to entered community."""
        )
    ),
    RouterOperation(
        path = "$API_VERSION_1/admin/community/{id}/members",
        method = arrayOf(RequestMethod.GET),
        beanClass = CommunityService::class,
        beanMethod = "listMembersForAdmin",
        operation = Operation(
            operationId = "GET admin/community/{id}/members",
            description = """List community members."""
        )
    ),
    RouterOperation(
        path = "$API_VERSION_1/admin/community/{id}/add",
        method = arrayOf(RequestMethod.POST),
        beanClass = CommunityService::class,
        beanMethod = "join",
        operation = Operation(
            operationId = "POST admin/community/{id}/add",
            description = """Add user to community members."""
        )
    ),
    RouterOperation(
        path = "$API_VERSION_1/admin/community/{id}/kick",
        method = arrayOf(RequestMethod.DELETE),
        beanClass = CommunityService::class,
        beanMethod = "leave",
        operation = Operation(
            operationId = "DELETE admin/community/{id}/kick",
            description = """Remove user from community members."""
        )
    ),
)
annotation class CommunityRoutingDoc