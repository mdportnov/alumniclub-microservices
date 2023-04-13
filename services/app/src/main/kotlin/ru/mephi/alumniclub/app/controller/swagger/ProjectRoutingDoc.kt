package ru.mephi.alumniclub.app.controller.swagger

import io.swagger.v3.oas.annotations.Operation
import org.springdoc.core.annotations.RouterOperation
import org.springdoc.core.annotations.RouterOperations
import org.springframework.web.bind.annotation.RequestMethod
import ru.mephi.alumniclub.app.service.ProjectService
import ru.mephi.alumniclub.shared.util.constants.API_VERSION_1

@SwaggerDocumentation
@RouterOperations(
    RouterOperation(
        path = "$API_VERSION_1/public/project",
        method = arrayOf(RequestMethod.GET),
        beanClass = ProjectService::class,
        beanMethod = "listProjects",
        operation = Operation(
            operationId = "GET public/project",
            description = """Returns list of projects with cursor pagination for not auth users."""
        )
    ),
    RouterOperation(
        path = "$API_VERSION_1/project/my",
        method = arrayOf(RequestMethod.GET),
        beanClass = ProjectService::class,
        beanMethod = "listProjectsByUser",
        operation = Operation(
            operationId = "GET project/my",
            description = """List projects in which the user participates with page pagination."""
        )
    ),
    RouterOperation(
        path = "$API_VERSION_1/project/{id}",
        method = arrayOf(RequestMethod.GET),
        beanClass = ProjectService::class,
        beanMethod = "findProjectById",
        operation = Operation(
            operationId = "GET project/{id}",
            description = """Returns full information about project by it id."""
        )
    ),
    RouterOperation(
        path = "$API_VERSION_1/project/{id}/join",
        method = arrayOf(RequestMethod.POST),
        beanClass = ProjectService::class,
        beanMethod = "join",
        operation = Operation(
            operationId = "POST project/{id}/join",
            description = """Join project."""
        )
    ),
    RouterOperation(
        path = "$API_VERSION_1/project/{id}/leave",
        method = arrayOf(RequestMethod.POST),
        beanClass = ProjectService::class,
        beanMethod = "leave",
        operation = Operation(
            operationId = "POST project/{id}/leave",
            description = """Leave project."""
        )
    ),
    RouterOperation(
        path = "$API_VERSION_1/project/{id}/members",
        method = arrayOf(RequestMethod.GET),
        beanClass = ProjectService::class,
        beanMethod = "listMembersForUser",
        operation = Operation(
            operationId = "GET project/{id}/members",
            description = """List project members."""
        )
    ),
    RouterOperation(
        path = "$API_VERSION_1/project/{id}/preview",
        method = arrayOf(RequestMethod.GET),
        beanClass = ProjectService::class,
        beanMethod = "previewProjectById",
        operation = Operation(
            operationId = "GET project/{id}/preview",
            description = """Preview project: get last 3 publications and 3 events."""
        )
    ),
    RouterOperation(
        path = "$API_VERSION_1/project",
        method = arrayOf(RequestMethod.GET),
        beanClass = ProjectService::class,
        beanMethod = "listProjects",
        operation = Operation(
            operationId = "GET project",
            description = """List all projects with cursor pagination."""
        )
    ),


    RouterOperation(
        path = "$API_VERSION_1/endowment/my",
        method = arrayOf(RequestMethod.GET),
        beanClass = ProjectService::class,
        beanMethod = "listEndowmentsByUser",
        operation = Operation(
            operationId = "GET endowment/my",
            description = """List endowments in which the user participates with page pagination."""
        )
    ),
    RouterOperation(
        path = "$API_VERSION_1/endowment",
        method = arrayOf(RequestMethod.GET),
        beanClass = ProjectService::class,
        beanMethod = "listEndowments",
        operation = Operation(
            operationId = "GET endowment",
            description = """List of all endowments with cursor pagination."""
        )
    ),
    RouterOperation(
        path = "$API_VERSION_1/endowment/{id}",
        method = arrayOf(RequestMethod.GET),
        beanClass = ProjectService::class,
        beanMethod = "findEndowmentById",
        operation = Operation(
            operationId = "GET endowment/{id}",
            description = """Returns full information about endowment by it id."""
        )
    ),
    RouterOperation(
        path = "$API_VERSION_1/endowment/{id}/join",
        method = arrayOf(RequestMethod.POST),
        beanClass = ProjectService::class,
        beanMethod = "join",
        operation = Operation(
            operationId = "POST endowment/{id}/join",
            description = """Join endowment."""
        )
    ),
    RouterOperation(
        path = "$API_VERSION_1/endowment/{id}/leave",
        method = arrayOf(RequestMethod.POST),
        beanClass = ProjectService::class,
        beanMethod = "leave",
        operation = Operation(
            operationId = "POST endowment/{id}/leave",
            description = """Leave endowment."""
        )
    ),
    RouterOperation(
        path = "$API_VERSION_1/endowment/{id}/members",
        method = arrayOf(RequestMethod.GET),
        beanClass = ProjectService::class,
        beanMethod = "listMembersForUser",
        operation = Operation(
            operationId = "GET endowment/{id}/members",
            description = """List endowment members."""
        )
    ),
    RouterOperation(
        path = "$API_VERSION_1/endowment/{id}/preview",
        method = arrayOf(RequestMethod.GET),
        beanClass = ProjectService::class,
        beanMethod = "previewEndowmentById",
        operation = Operation(
            operationId = "GET endowment/{id}/preview",
            description = """Preview endowment: get last 3 publications and 3 events."""
        )
    ),


    RouterOperation(
        path = "$API_VERSION_1/admin/project",
        method = arrayOf(RequestMethod.GET),
        beanClass = ProjectService::class,
        beanMethod = "listProjectsForAdmin",
        operation = Operation(
            operationId = "GET admin/project",
            description = """List projects."""
        )
    ),
    RouterOperation(
        path = "$API_VERSION_1/admin/project",
        method = arrayOf(RequestMethod.POST),
        beanClass = ProjectService::class,
        beanMethod = "createProject",
        operation = Operation(
            operationId = "POST admin/project",
            description = """Create project."""
        )
    ),
    RouterOperation(
        path = "$API_VERSION_1/admin/project/{id}",
        method = arrayOf(RequestMethod.GET),
        beanClass = ProjectService::class,
        beanMethod = "findProjectById",
        operation = Operation(
            operationId = "GET admin/project/{id}",
            description = """Get project information."""
        )
    ),
    RouterOperation(
        path = "$API_VERSION_1/admin/project/{id}",
        method = arrayOf(RequestMethod.PUT),
        beanClass = ProjectService::class,
        beanMethod = "updateProject",
        operation = Operation(
            operationId = "PUT admin/project/{id}",
            description = """Update project information."""
        )
    ),
    RouterOperation(
        path = "$API_VERSION_1/admin/project/{id}/photo",
        method = arrayOf(RequestMethod.POST),
        beanClass = ProjectService::class,
        beanMethod = "uploadPhoto",
        operation = Operation(
            operationId = "POST admin/project/{id}/photo",
            description = """Upload project photo."""
        )
    ),
    RouterOperation(
        path = "$API_VERSION_1/admin/project/{id}",
        method = arrayOf(RequestMethod.DELETE),
        beanClass = ProjectService::class,
        beanMethod = "delete",
        operation = Operation(
            operationId = "DELETE admin/project/{id}",
            description = """Delete project."""
        )
    ),
    RouterOperation(
        path = "$API_VERSION_1/admin/project/{id}/members",
        method = arrayOf(RequestMethod.GET),
        beanClass = ProjectService::class,
        beanMethod = "listMembersForAdmin",
        operation = Operation(
            operationId = "GET admin/project/{id}/members",
            description = """List project members."""
        )
    ),
    RouterOperation(
        path = "$API_VERSION_1/admin/project/{id}/archive",
        method = arrayOf(RequestMethod.POST),
        beanClass = ProjectService::class,
        beanMethod = "archive",
        operation = Operation(
            operationId = "POST admin/project/{id}/archive",
            description = """Change project archive status."""
        )
    ),

    RouterOperation(
        path = "$API_VERSION_1/admin/endowment",
        method = arrayOf(RequestMethod.GET),
        beanClass = ProjectService::class,
        beanMethod = "listEndowmentsForAdmin",
        operation = Operation(
            operationId = "GET admin/endowment",
            description = """List endowments."""
        )
    ),
    RouterOperation(
        path = "$API_VERSION_1/admin/endowment",
        method = arrayOf(RequestMethod.POST),
        beanClass = ProjectService::class,
        beanMethod = "createEndowment",
        operation = Operation(
            operationId = "POST admin/endowment",
            description = """Create endowment."""
        )
    ),
    RouterOperation(
        path = "$API_VERSION_1/admin/endowment/{id}",
        method = arrayOf(RequestMethod.GET),
        beanClass = ProjectService::class,
        beanMethod = "findEndowmentById",
        operation = Operation(
            operationId = "GET admin/endowment/{id}",
            description = """Get endowment information."""
        )
    ),
    RouterOperation(
        path = "$API_VERSION_1/admin/endowment/{id}",
        method = arrayOf(RequestMethod.PUT),
        beanClass = ProjectService::class,
        beanMethod = "updateEndowment",
        operation = Operation(
            operationId = "PUT admin/endowment/{id}",
            description = """Update endowment information."""
        )
    ),
    RouterOperation(
        path = "$API_VERSION_1/admin/endowment/{id}/photo",
        method = arrayOf(RequestMethod.POST),
        beanClass = ProjectService::class,
        beanMethod = "uploadPhoto",
        operation = Operation(
            operationId = "POST admin/endowment/{id}/photo",
            description = """Upload endowment photo."""
        )
    ),
    RouterOperation(
        path = "$API_VERSION_1/admin/endowment/{id}",
        method = arrayOf(RequestMethod.DELETE),
        beanClass = ProjectService::class,
        beanMethod = "delete",
        operation = Operation(
            operationId = "DELETE admin/endowment/{id}",
            description = """Delete endowment."""
        )
    ),
    RouterOperation(
        path = "$API_VERSION_1/admin/endowment/{id}/members",
        method = arrayOf(RequestMethod.GET),
        beanClass = ProjectService::class,
        beanMethod = "listMembersForAdmin",
        operation = Operation(
            operationId = "GET admin/endowment/{id}/members",
            description = """List endowment members."""
        )
    ),
    RouterOperation(
        path = "$API_VERSION_1/admin/endowment/{id}/archive",
        method = arrayOf(RequestMethod.POST),
        beanClass = ProjectService::class,
        beanMethod = "archive",
        operation = Operation(
            operationId = "POST admin/endowment/{id}/archive",
            description = """Change endowment archive status."""
        )
    ),
)
annotation class ProjectRoutingDoc