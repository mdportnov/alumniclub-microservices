package ru.mephi.alumniclub.app.controller.swagger

import io.swagger.v3.oas.annotations.Operation
import org.springdoc.core.annotations.RouterOperation
import org.springdoc.core.annotations.RouterOperations
import org.springframework.web.bind.annotation.RequestMethod
import ru.mephi.alumniclub.app.service.ExcelService
import ru.mephi.alumniclub.shared.util.constants.API_VERSION_1

@SwaggerDocumentation
@RouterOperations(
    RouterOperation(
        path = "$API_VERSION_1/util/export",
        method = arrayOf(RequestMethod.GET),
        beanClass = ExcelService::class,
        beanMethod = "exportUsers",
        operation = Operation(
            operationId = "util/export",
            description = """
                Returns excel file (*.xlsx) with all information about users from db 
                (+ alumnusData, MentorData, lyceumData).
                """"
        )
    ),
)
annotation class ExcelRoutingDoc