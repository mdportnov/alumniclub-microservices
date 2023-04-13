package ru.mephi.alumniclub.app.controller.swagger

import io.swagger.v3.oas.annotations.Operation
import org.springdoc.core.annotations.RouterOperation
import org.springdoc.core.annotations.RouterOperations
import org.springframework.web.bind.annotation.RequestMethod
import ru.mephi.alumniclub.app.service.PushService
import ru.mephi.alumniclub.shared.util.constants.API_VERSION_1


@SwaggerDocumentation
@RouterOperations(
    RouterOperation(
        path = "$API_VERSION_1/admin/push/send",
        method = arrayOf(RequestMethod.POST),
        beanClass = PushService::class,
        beanMethod = "sendSimpleTextNotification",
        operation = Operation(
            operationId = "admin/push/send",
            description = """Send push notification to some users."""
        )
    )
)
annotation class PushRoutingDoc