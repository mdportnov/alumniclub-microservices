package ru.mephi.alumniclub.app.controller.handler.impl

import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse
import org.springframework.web.servlet.function.ServerResponse.*
import org.springframework.web.servlet.function.body
import org.springframework.web.servlet.function.paramOrNull
import org.thymeleaf.spring5.SpringTemplateEngine
import ru.mephi.alumniclub.app.controller.handler.AuthHandler
import ru.mephi.alumniclub.app.model.dto.auth.request.LoginRequest
import ru.mephi.alumniclub.app.model.dto.auth.request.RefreshRequest
import ru.mephi.alumniclub.app.model.dto.auth.RegistrationSuccessDTO
import ru.mephi.alumniclub.app.model.dto.auth.password.request.RefreshPasswordByEmailRequest
import ru.mephi.alumniclub.app.model.dto.auth.password.request.ResetPasswordRequest
import ru.mephi.alumniclub.app.model.dto.auth.password.request.SendEmailToResetPasswordRequest
import ru.mephi.alumniclub.app.model.dto.user.request.RegistrationRequest
import ru.mephi.alumniclub.app.service.*
import ru.mephi.alumniclub.app.util.RefreshCookieGenerator
import ru.mephi.alumniclub.shared.dto.common.ApiError
import ru.mephi.alumniclub.shared.dto.common.ApiMessage
import ru.mephi.alumniclub.shared.model.exceptions.common.CorruptedTokenException
import ru.mephi.alumniclub.shared.util.constants.REFRESH_TOKEN_COOKIE
import ru.mephi.alumniclub.shared.util.extension.paramOrThrow
import ru.mephi.alumniclub.shared.util.extension.toCreatedResponse
import ru.mephi.alumniclub.shared.util.extension.toOkBody
import ru.mephi.alumniclub.shared.util.response.ResponseManager


@Component
class AuthHandlerImpl(
    private val templateEngine: SpringTemplateEngine,
    private val refreshCookieGenerator: RefreshCookieGenerator,

    private val authService: AuthService,
    private val featureToggleService: FeatureToggleService,
    private val resetPasswordService: ResetPasswordService,
    private val registrationLimiter: RegistrationLimiterService,
    private val verifyEmailService: VerifyEmailService,

    @Value("\${verifyEmail.tokenTtl}") private val verifyEmailTokenTtl: Long
) : ResponseManager(), AuthHandler {

    override fun register(request: ServerRequest): ServerResponse {
        registrationLimiter.onRegistration()
        val registrationRequest = request.body<RegistrationRequest>()

        validate(registrationRequest)

        val referralToken = request.paramOrNull("referralToken")
        val user = authService.register(registrationRequest, referralToken)

        val verifyEmailEnabled = featureToggleService.isFeatureEnabled("verifyEmail")
        val message = if (verifyEmailEnabled)
            i18n("label.auth.registrationWithVerification", "$verifyEmailTokenTtl")
        else i18n("label.auth.registrationWithoutVerification")

        return ApiMessage(
            message,
            RegistrationSuccessDTO(user.id, verifyEmailEnabled)
        ).toCreatedResponse("/api/v1/user/${user.id}")
    }

    override fun login(request: ServerRequest): ServerResponse {
        val loginRequest = request.body<LoginRequest>()
        validate(loginRequest)
        val (tokenResponse, refreshToken) = authService.login(loginRequest)
        val response = ApiMessage(data = tokenResponse, message = i18n("label.auth.login"))
        return ok().cookie(refreshCookieGenerator.generateRefreshCookie(refreshToken)).body(response)
    }

    override fun refresh(request: ServerRequest): ServerResponse {
        val refreshToken = request.cookies().getFirst(REFRESH_TOKEN_COOKIE) ?: throw CorruptedTokenException()
        val refreshRequest = request.body<RefreshRequest>()
        validate(refreshRequest)
        val (response, newRefreshToken) = authService.refresh(refreshRequest, refreshToken.value)
        return ok().cookie(refreshCookieGenerator.generateRefreshCookie(newRefreshToken)).body(response)
    }

    override fun logout(request: ServerRequest): ServerResponse {
        val refreshToken = request.cookies().getFirst(REFRESH_TOKEN_COOKIE) ?: throw CorruptedTokenException()
        authService.logout(refreshToken.value)
        val response = ApiMessage(data = null, message = i18n("label.auth.logout"))
        return ok().cookie(refreshCookieGenerator.generateRefreshCookie(maxAge = 0)).body(response)
    }

    override fun resetPassword(request: ServerRequest): ServerResponse {
        val resetRequest = request.body<SendEmailToResetPasswordRequest>()
        validate(resetRequest)
        authService.sendEmailToResetPassword(resetRequest)
        return ApiMessage(data = null, message = i18n("label.auth.resetPasswordLetterSent")).toOkBody()
    }

    override fun resetRefreshPassword(request: ServerRequest): ServerResponse {
        val token = request.paramOrThrow<String>("token")
        val resetPasswordRequest = request.body<ResetPasswordRequest>().apply { this.token = token }
        validate(resetPasswordRequest)
        val (tokenResponse, refreshToken) = authService.resetRefreshPassword(resetPasswordRequest)
        val response = ApiMessage(data = tokenResponse, message = i18n("label.auth.passwordChanged"))
        return ok().cookie(refreshCookieGenerator.generateRefreshCookie(refreshToken)).body(response)
    }

    override fun checkResetPasswordTokenExists(request: ServerRequest): ServerResponse {
        val token = request.paramOrThrow<String>("token")
        return if (resetPasswordService.tokenIsExist(token)) {
            ok().body(ApiMessage(data = null, message = i18n("label.auth.tokenExists")))
        } else status(HttpStatus.NOT_FOUND).body(
            ApiMessage(data = null, message = i18n("label.auth.tokenNotExists"))
        )
    }

    override fun refreshPassword(request: ServerRequest): ServerResponse {
        val setPasswordRequest = request.body<RefreshPasswordByEmailRequest>()
        validate(setPasswordRequest)
        authService.refreshPassword(setPasswordRequest)
        val response = ApiMessage(data = null, message = i18n("label.auth.passwordChanged"))
        return ok().cookie(refreshCookieGenerator.generateRefreshCookie(maxAge = 0)).body(response)
    }

    override fun verifyEmail(request: ServerRequest): ServerResponse {
        val token = request.param("token").get()
        val context = org.thymeleaf.context.Context()
        try {
            verifyEmailService.verifyEmail(token)
            context.setVariable("message", i18n("label.auth.emailVerified"))
        } catch (e: ApiError) {
            context.setVariable("message", i18n("exception.auth.emailAlreadyVerified"))
        }
        val page = templateEngine.process("pages/VerifyEmail.html", context)
        return ok().contentType(MediaType.TEXT_HTML).body(page)
    }
}
