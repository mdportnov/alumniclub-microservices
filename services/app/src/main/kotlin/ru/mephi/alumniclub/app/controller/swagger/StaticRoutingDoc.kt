package ru.mephi.alumniclub.app.controller.swagger

import io.swagger.v3.oas.annotations.Operation
import org.springdoc.core.annotations.RouterOperation
import org.springdoc.core.annotations.RouterOperations
import org.springframework.web.bind.annotation.RequestMethod
import ru.mephi.alumniclub.app.service.StorageManager
import ru.mephi.alumniclub.shared.util.constants.API_VERSION_1

@SwaggerDocumentation
@RouterOperations(
    RouterOperation(
        path = "$API_VERSION_1/uploads/{dir}/{fileName}",
        method = arrayOf(RequestMethod.GET),
        beanClass = StorageManager::class,
        beanMethod = "retrieve",
        operation = Operation(
            operationId = "uploads/{dir}/{fileName}",
            description = """Returns file from server storage."""
        )
    )
)
annotation class StaticRoutingDoc