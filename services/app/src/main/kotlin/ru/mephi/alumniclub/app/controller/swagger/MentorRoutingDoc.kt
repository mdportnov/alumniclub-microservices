package ru.mephi.alumniclub.app.controller.swagger

import io.swagger.v3.oas.annotations.Operation
import org.springdoc.core.annotations.RouterOperation
import org.springdoc.core.annotations.RouterOperations
import org.springframework.web.bind.annotation.RequestMethod
import ru.mephi.alumniclub.app.service.MentorService
import ru.mephi.alumniclub.shared.util.constants.API_VERSION_1

@SwaggerDocumentation
@RouterOperations(
    RouterOperation(
        path = "$API_VERSION_1/mentor/{id}",
        method = arrayOf(RequestMethod.GET),
        beanClass = MentorService::class,
        beanMethod = "getMentorByUserId",
        operation = Operation(
            operationId = "mentor/{id}",
            description = """Returns full information about entered mentor."""
        )
    ),
    RouterOperation(
        path = "$API_VERSION_1/mentor",
        method = arrayOf(RequestMethod.GET),
        beanClass = MentorService::class,
        beanMethod = "listAllAvailableMentors",
        operation = Operation(
            operationId = "mentor",
            description = """Returns short information about all available mentors."""
        )
    ),
    RouterOperation(
        path = "$API_VERSION_1/admin/mentor/{id}/available",
        method = arrayOf(RequestMethod.POST),
        beanClass = MentorService::class,
        beanMethod = "toggleMentorAvailability",
        operation = Operation(
            operationId = "admin/mentor/{id}/available",
            description = """Set new availability to entered mentor."""
        )
    ),
    RouterOperation(
        path = "$API_VERSION_1/admin/mentor/{id}",
        method = arrayOf(RequestMethod.GET),
        beanClass = MentorService::class,
        beanMethod = "getMentorByUserId",
        operation = Operation(
            operationId = "GET admin/mentor/{id}",
            description = """Returns full information about entered mentor."""
        )
    ),
    RouterOperation(
        path = "$API_VERSION_1/admin/mentor/{id}",
        method = arrayOf(RequestMethod.DELETE),
        beanClass = MentorService::class,
        beanMethod = "deleteUserMentor",
        operation = Operation(
            operationId = "DELETE admin/mentor/{id}",
            description = """Deletes entered mentor."""
        )
    ),
    RouterOperation(
        path = "$API_VERSION_1/admin/mentor",
        method = arrayOf(RequestMethod.GET),
        beanClass = MentorService::class,
        beanMethod = "listAllMentors",
        operation = Operation(
            operationId = "GET admin/mentor",
            description = """Returns short information about all mentors."""
        )
    ),
    RouterOperation(
        path = "$API_VERSION_1/admin/mentor",
        method = arrayOf(RequestMethod.POST),
        beanClass = MentorService::class,
        beanMethod = "saveUserMentorInfo",
        operation = Operation(
            operationId = "POST admin/mentor",
            description = """Create new mentor."""
        )
    ),
    RouterOperation(
        path = "$API_VERSION_1/admin/mentor",
        method = arrayOf(RequestMethod.PUT),
        beanClass = MentorService::class,
        beanMethod = "updateUserMentorInfo",
        operation = Operation(
            operationId = "PUT admin/mentor",
            description = """Update information about mentor."""
        )
    ),
)
annotation class MentorRoutingDoc