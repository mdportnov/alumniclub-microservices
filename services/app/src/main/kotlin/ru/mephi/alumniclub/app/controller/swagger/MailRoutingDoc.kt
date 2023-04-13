package ru.mephi.alumniclub.app.controller.swagger

import io.swagger.v3.oas.annotations.Operation
import org.springdoc.core.annotations.RouterOperation
import org.springdoc.core.annotations.RouterOperations
import org.springframework.web.bind.annotation.RequestMethod
import ru.mephi.alumniclub.app.service.EmailService
import ru.mephi.alumniclub.shared.util.constants.API_VERSION_1


@SwaggerDocumentation
@RouterOperations(
    RouterOperation(
        path = "$API_VERSION_1/admin/mail/textmail",
        method = arrayOf(RequestMethod.POST),
        beanClass = EmailService::class,
        beanMethod = "sendTextMail",
        operation = Operation(
            operationId = "admin/mail/textmail",
            description = """Sends text email massage to some users."""
        )
    )
)
annotation class MailRoutingDoc