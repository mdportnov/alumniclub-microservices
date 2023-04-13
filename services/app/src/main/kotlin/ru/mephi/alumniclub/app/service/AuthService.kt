package ru.mephi.alumniclub.app.service

import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.parameters.RequestBody
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.RequestParam
import ru.mephi.alumniclub.app.model.dto.auth.response.AccessTokenResponse
import ru.mephi.alumniclub.app.model.dto.auth.request.LoginRequest
import ru.mephi.alumniclub.app.model.dto.auth.request.RefreshRequest
import ru.mephi.alumniclub.app.model.dto.auth.password.request.RefreshPasswordByEmailRequest
import ru.mephi.alumniclub.app.model.dto.auth.password.request.RefreshPasswordRequest
import ru.mephi.alumniclub.app.model.dto.auth.password.request.ResetPasswordRequest
import ru.mephi.alumniclub.app.model.dto.auth.password.request.SendEmailToResetPasswordRequest
import ru.mephi.alumniclub.app.model.dto.user.request.RegistrationRequest
import ru.mephi.alumniclub.app.model.dto.user.response.UserResponse

@Tag(name = "Auth API")
interface AuthService {
    fun register(@RequestBody request: RegistrationRequest, @RequestParam referralToken: String? = null): UserResponse
    fun login(@RequestBody request: LoginRequest): Pair<AccessTokenResponse, String>
    fun refresh(@RequestBody fingerprint: RefreshRequest, refreshToken: String): Pair<AccessTokenResponse, String>
    fun logout(@Parameter(hidden = true) refreshToken: String)
    fun sendEmailToResetPassword(@RequestBody request: SendEmailToResetPasswordRequest)
    fun resetRefreshPassword(@RequestBody request: ResetPasswordRequest): Pair<AccessTokenResponse, String>
    fun refreshPassword(@RequestBody request: RefreshPasswordByEmailRequest)
    fun refreshPassword(@Parameter(hidden = true) userId: Long, @RequestBody request: RefreshPasswordRequest)
}
