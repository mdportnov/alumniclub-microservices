package ru.mephi.alumniclub.app.controller.swagger

import io.swagger.v3.oas.annotations.Operation
import org.springdoc.core.annotations.RouterOperation
import org.springdoc.core.annotations.RouterOperations
import org.springframework.web.bind.annotation.RequestMethod
import ru.mephi.alumniclub.app.service.FcmTokenService
import ru.mephi.alumniclub.shared.util.constants.API_VERSION_1

@SwaggerDocumentation
@RouterOperations(
    RouterOperation(
        path = "$API_VERSION_1/fcm/tokens/upload",
        method = arrayOf(RequestMethod.POST),
        beanClass = FcmTokenService::class,
        beanMethod = "uploadToken",
        operation = Operation(
            operationId = "fcm/tokens/upload",
            description = """Uploads user fcm token."""
        )
    ),
    RouterOperation(
        path = "$API_VERSION_1/fcm/tokens/remove",
        method = arrayOf(RequestMethod.DELETE),
        beanClass = FcmTokenService::class,
        beanMethod = "removeToken",
        operation = Operation(
            operationId = "fcm/tokens/removeToken",
            description = """Removes user fcm token."""
        )
    )
)
annotation class FcmTokenRoutingDoc