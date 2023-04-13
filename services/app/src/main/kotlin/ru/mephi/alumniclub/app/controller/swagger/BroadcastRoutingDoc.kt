package ru.mephi.alumniclub.app.controller.swagger

import io.swagger.v3.oas.annotations.Operation
import org.springdoc.core.annotations.RouterOperation
import org.springdoc.core.annotations.RouterOperations
import org.springframework.web.bind.annotation.RequestMethod
import ru.mephi.alumniclub.app.service.BroadcastSenderService


@SwaggerDocumentation
@RouterOperations(
    RouterOperation(
        path = "/v1/admin/broadcast",
        method = arrayOf(RequestMethod.POST),
        beanClass = BroadcastSenderService::class,
        beanMethod = "createBroadcast",
        operation = Operation(
            operationId = "POST admin/broadcast",
            description = """
                Create broadcast to something notification channels (email, push, notification in app)
                with using users preferences. Choose notification by DistributionOptions
                (in options by default: sendPush = false, sendEmail = false, sendNotification = true, ignorePreferences = false).
            """
        )
    ),
    RouterOperation(
        path = "???/???",
        method = arrayOf(RequestMethod.POST),
        beanClass = BroadcastSenderService::class,
        beanMethod = "createBroadcast",
        operation = Operation(
            operationId = "POST ???/???",
            description = """
                Not see to this method! It's garbage.
            """
        )
    )
)
annotation class BroadcastRoutingDoc