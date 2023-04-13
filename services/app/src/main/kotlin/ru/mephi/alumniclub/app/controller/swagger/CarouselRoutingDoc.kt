package ru.mephi.alumniclub.app.controller.swagger

import io.swagger.v3.oas.annotations.Operation
import org.springdoc.core.annotations.RouterOperation
import org.springdoc.core.annotations.RouterOperations
import org.springframework.web.bind.annotation.RequestMethod
import ru.mephi.alumniclub.app.service.CarouselService
import ru.mephi.alumniclub.shared.util.constants.API_VERSION_1

@SwaggerDocumentation
@RouterOperations(
    RouterOperation(
        path = "$API_VERSION_1/news/{id}",
        method = arrayOf(RequestMethod.GET),
        beanClass = CarouselService::class,
        beanMethod = "getById",
        operation = Operation(
            operationId = "GET news/{id}",
            description = """Returns news by it id for user."""
        )
    ),
    RouterOperation(
        path = "$API_VERSION_1/news",
        method = arrayOf(RequestMethod.GET),
        beanClass = CarouselService::class,
        beanMethod = "getAll",
        operation = Operation(
            operationId = "GET news",
            description = """Returns all news for user."""
        )
    ),
    RouterOperation(
        path = "$API_VERSION_1/admin/news",
        method = arrayOf(RequestMethod.GET),
        beanClass = CarouselService::class,
        beanMethod = "getAll",
        operation = Operation(
            operationId = "GET admin/news",
            description = """Returns all news for admin."""
        )
    ),
    RouterOperation(
        path = "$API_VERSION_1/admin/news/{id}",
        method = arrayOf(RequestMethod.GET),
        beanClass = CarouselService::class,
        beanMethod = "getById",
        operation = Operation(
            operationId = "GET admin/news/{id}",
            description = """Returns news by it id for admin."""
        )
    ),
    RouterOperation(
        path = "$API_VERSION_1/admin/news/{id}",
        method = arrayOf(RequestMethod.PUT),
        beanClass = CarouselService::class,
        beanMethod = "update",
        operation = Operation(
            operationId = "PUT admin/news/{id}",
            description = """Changes news by it id."""
        )
    ),
    RouterOperation(
        path = "$API_VERSION_1/admin/news/{id}/photo",
        method = arrayOf(RequestMethod.POST),
        beanClass = CarouselService::class,
        beanMethod = "uploadPhoto",
        operation = Operation(
            operationId = "PUT admin/news/{id}/photo",
            description = """Upload photo to news."""
        )
    ),
    RouterOperation(
        path = "$API_VERSION_1/admin/news/{id}",
        method = arrayOf(RequestMethod.DELETE),
        beanClass = CarouselService::class,
        beanMethod = "deleteById",
        operation = Operation(
            operationId = "DELETE admin/news/{id}",
            description = """Removes news by it id."""
        )
    ),
    RouterOperation(
        path = "$API_VERSION_1/admin/news",
        method = arrayOf(RequestMethod.POST),
        beanClass = CarouselService::class,
        beanMethod = "create",
        operation = Operation(
            operationId = "POST admin/news",
            description = """Creates new news."""
        )
    )
)
annotation class CarouselRoutingDoc