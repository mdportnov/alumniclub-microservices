package ru.mephi.alumniclub.app.controller.swagger

import io.swagger.v3.oas.annotations.Operation
import org.springdoc.core.annotations.RouterOperation
import org.springdoc.core.annotations.RouterOperations
import org.springframework.web.bind.annotation.RequestMethod
import ru.mephi.alumniclub.app.service.SurveyService
import ru.mephi.alumniclub.shared.util.constants.API_VERSION_1

@SwaggerDocumentation
@RouterOperations(
    RouterOperation(
        path = "$API_VERSION_1/survey/list",
        method = arrayOf(RequestMethod.GET),
        beanClass = SurveyService::class,
        beanMethod = "list",
        operation = Operation(
            operationId = "survey/list",
            description = """Returns to user list of surveys (+ short information about answers + self answer)
                with cursor pagination. Not returns ends surveys"""
        )
    ),
    RouterOperation(
        path = "$API_VERSION_1/survey/{id}",
        method = arrayOf(RequestMethod.GET),
        beanClass = SurveyService::class,
        beanMethod = "getSurveyById",
        operation = Operation(
            operationId = "survey/{id}",
            description = """Returns to user full survey + short information about answers + self answer."""
        )
    ),
    RouterOperation(
        path = "$API_VERSION_1/survey/{id}/vote",
        method = arrayOf(RequestMethod.POST),
        beanClass = SurveyService::class,
        beanMethod = "voteInSurvey",
        operation = Operation(
            operationId = "survey/{id}/vote",
            description = """Vote in survey. Returns short information about answers + self answer."""
        )
    ),
    RouterOperation(
        path = "$API_VERSION_1/survey/{id}/answers/me",
        method = arrayOf(RequestMethod.GET),
        beanClass = SurveyService::class,
        beanMethod = "getUserAnswer",
        operation = Operation(
            operationId = "survey/{id}/answers/me",
            description = """Returns to user self answer."""
        )
    ),
    RouterOperation(
        path = "$API_VERSION_1/survey/{id}/info",
        method = arrayOf(RequestMethod.GET),
        beanClass = SurveyService::class,
        beanMethod = "getSurveyAnswersInfo",
        operation = Operation(
            operationId = "survey/{id}/info",
            description = """Returns to user short information about answers."""
        )
    ),
    RouterOperation(
        path = "$API_VERSION_1/survey/{id}/answer-with-info",
        method = arrayOf(RequestMethod.GET),
        beanClass = SurveyService::class,
        beanMethod = "getSurveyMetadata",
        operation = Operation(
            operationId = "survey/{id}/answer-with-info",
            description = """Returns to user short information about answers + self answer."""
        )
    ),
    RouterOperation(
        path = "$API_VERSION_1/admin/survey/list",
        method = arrayOf(RequestMethod.GET),
        beanClass = SurveyService::class,
        beanMethod = "getSurveyMetadata",
        operation = Operation(
            operationId = "admin/survey/list",
            description = """Returns to admin list of surveys (+ short information about answers + self answer)."""
        )
    ),
    RouterOperation(
        path = "$API_VERSION_1/admin/survey",
        method = arrayOf(RequestMethod.POST),
        beanClass = SurveyService::class,
        beanMethod = "create",
        operation = Operation(
            operationId = "admin/survey",
            description = """Creates new survey and returns it."""
        )
    ),
    RouterOperation(
        path = "$API_VERSION_1/admin/survey/{id}/answers/{userId}",
        method = arrayOf(RequestMethod.GET),
        beanClass = SurveyService::class,
        beanMethod = "getUserAnswer",
        operation = Operation(
            operationId = "admin/survey/{id}/answers/{userId}",
            description = """Returns answer of entered user to entered survey."""
        )
    ),
    RouterOperation(
        path = "$API_VERSION_1/admin/survey/{id}",
        method = arrayOf(RequestMethod.DELETE),
        beanClass = SurveyService::class,
        beanMethod = "remove",
        operation = Operation(
            operationId = "DELETE admin/survey/{id}",
            description = """Removes entered survey and all answers to it."""
        )
    ),
    RouterOperation(
        path = "$API_VERSION_1/admin/survey/{id}",
        method = arrayOf(RequestMethod.PUT),
        beanClass = SurveyService::class,
        beanMethod = "updateSurveyById",
        operation = Operation(
            operationId = "PUT admin/survey/{id}",
            description = """Updates entered survey."""
        )
    )
)
annotation class SurveyRoutingDoc