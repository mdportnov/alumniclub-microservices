package ru.mephi.alumniclub.app.controller.swagger

import io.swagger.v3.oas.annotations.Operation
import org.springdoc.core.annotations.RouterOperation
import org.springdoc.core.annotations.RouterOperations
import org.springframework.web.bind.annotation.RequestMethod
import ru.mephi.alumniclub.app.service.AuthService
import ru.mephi.alumniclub.shared.util.constants.API_VERSION_1

@SwaggerDocumentation
@RouterOperations(
    RouterOperation(
        path = "$API_VERSION_1/public/auth/register",
        method = arrayOf(RequestMethod.POST),
        beanClass = AuthService::class,
        beanMethod = "register",
        operation = Operation(
            operationId = "POST auth/register",
            description = """Register user. To get JWT you have to login."""
        )
    ),
    RouterOperation(
        path = "$API_VERSION_1/public/auth/login",
        method = arrayOf(RequestMethod.POST),
        beanClass = AuthService::class,
        beanMethod = "login",
        operation = Operation(
            operationId = "POST auth/login",
            description = """Login user by email and password. Get JWT for next authentication.""",
        )
    ),
    RouterOperation(
        path = "$API_VERSION_1/public/auth/admin",
        method = arrayOf(RequestMethod.POST),
        beanClass = AuthService::class,
        beanMethod = "loginAdmin",
        operation = Operation(
            operationId = "POST auth/admin",
            description = """Login user with admin role by email and password. Get JWT for next authentication."""
        )
    ),
    RouterOperation(
        path = "$API_VERSION_1/public/auth/refresh",
        method = arrayOf(RequestMethod.POST),
        beanClass = AuthService::class,
        beanMethod = "refresh",
        operation = Operation(
            operationId = "POST auth/admin",
            description = """Gets refresh token from request. Returns to user new pair of access/refresh tokens."""
        )
    ),
    RouterOperation(
        path = "$API_VERSION_1/public/auth/reset-password-by-email",
        method = arrayOf(RequestMethod.POST),
        beanClass = AuthService::class,
        beanMethod = "sendEmailToResetPassword",
        operation = Operation(
            operationId = "auth/reset-password-by-email",
            description = """If user forgot his password he can reset it by email.
                    This request send to email special link to reset password."""
        )
    ),
    RouterOperation(
        path = "$API_VERSION_1/public/auth/accept-reset-password",
        method = arrayOf(RequestMethod.POST),
        beanClass = AuthService::class,
        beanMethod = "resetPassword",
        operation = Operation(
            operationId = "auth/accept-reset-password",
            description = """If the user sent a password reset request, click on the site link (from their email),
                    enter a new password and confirm it, will be do this request."""
        )
    ),
    RouterOperation(
        path = "$API_VERSION_1/public/auth/reset-password-token-exist",
        method = arrayOf(RequestMethod.GET),
        beanClass = AuthService::class,
        beanMethod = "tokenIsExist",
        operation = Operation(
            operationId = "auth/reset-password-token-exist",
            description = """Returns response with empty body.
                If entered token to reset password exist then code is 200, else is 404."""
        )
    ),
    RouterOperation(
        path = "$API_VERSION_1/public/auth/password/new",
        method = arrayOf(RequestMethod.POST),
        beanClass = AuthService::class,
        beanMethod = "setNewPassword",
        operation = Operation(
            operationId = "auth/password/new",
            description = """If user doesn't like his password he can set new. For it he enter old and new password"""
        )
    ),
    RouterOperation(
        path = "$API_VERSION_1/public/auth/verify-email",
        method = arrayOf(RequestMethod.GET),
        beanClass = AuthService::class,
        beanMethod = "verifyEmail",
        operation = Operation(
            operationId = "auth/verify-email",
            description = """When a user registers a new account, they must confirm the entered email address.
                    When registering, a special link will be sent to his email, 
                    if the user clicks on it, this request will be do."""
        )
    )
)
annotation class AuthRoutingDoc