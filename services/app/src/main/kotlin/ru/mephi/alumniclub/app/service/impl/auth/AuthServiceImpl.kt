package ru.mephi.alumniclub.app.service.impl.auth

import com.google.api.client.auth.oauth2.TokenResponse
import org.springframework.data.jpa.repository.Modifying
import org.springframework.http.HttpStatus
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import ru.mephi.alumniclub.app.database.entity.email.ResetPasswordToken
import ru.mephi.alumniclub.app.database.entity.user.User
import ru.mephi.alumniclub.app.database.repository.email.ResetPasswordTokenDao
import ru.mephi.alumniclub.app.database.repository.user.BioDao
import ru.mephi.alumniclub.app.database.repository.user.DegreeDao
import ru.mephi.alumniclub.app.database.repository.user.UserDao
import ru.mephi.alumniclub.app.model.dto.auth.password.request.RefreshPasswordByEmailRequest
import ru.mephi.alumniclub.app.model.dto.auth.password.request.RefreshPasswordRequest
import ru.mephi.alumniclub.app.model.dto.auth.password.request.ResetPasswordRequest
import ru.mephi.alumniclub.app.model.dto.auth.password.request.SendEmailToResetPasswordRequest
import ru.mephi.alumniclub.app.model.dto.auth.request.LoginRequest
import ru.mephi.alumniclub.app.model.dto.auth.request.RefreshRequest
import ru.mephi.alumniclub.app.model.dto.auth.response.AccessTokenResponse
import ru.mephi.alumniclub.app.model.dto.user.request.RegistrationRequest
import ru.mephi.alumniclub.app.model.dto.user.response.UserResponse
import ru.mephi.alumniclub.app.model.enumeration.referral.ReferralStatus
import ru.mephi.alumniclub.app.model.mapper.user.UserMapper
import ru.mephi.alumniclub.app.service.*
import ru.mephi.alumniclub.app.service.impl.auth.token.TokenManager
import ru.mephi.alumniclub.shared.dto.common.ApiError
import ru.mephi.alumniclub.shared.dto.mail.ResetPasswordMail
import ru.mephi.alumniclub.shared.util.response.ResponseManager
import java.time.LocalDateTime
import javax.transaction.Transactional

@Service
@Transactional
class AuthServiceImpl(
    // Managers
    private val passwordEncoder: PasswordEncoder,
    private val tokenManager: TokenManager,
    private val rabbitService: RabbitService,

    // DAOs
    private val userDao: UserDao,
    private val bioDao: BioDao,
    private val degreeDao: DegreeDao,
    private val resetPasswordTokenDao: ResetPasswordTokenDao,
    private val userMapper: UserMapper,
    private val userService: UserService,
    private val verifyEmailService: VerifyEmailService,
    private val resetPasswordService: ResetPasswordService,
    private val featureToggleService: FeatureToggleService,
    private val userPreferencesService: UserPreferencesService,
    private val referralUserService: ReferralUserService,
    private val affiliateService: AffiliateService
) : ResponseManager(), AuthService {

    @Modifying
    override fun register(request: RegistrationRequest, referralToken: String?): UserResponse {
        if (userDao.existsByEmail(request.email))
            throw ApiError(HttpStatus.BAD_REQUEST, i18n("exception.alreadyExists.email", request.email))
        validateRegistration(request)
        val user = userService.createUser(request).also { initialUserSetup(it, referralToken) }
        if (featureToggleService.isFeatureEnabled("verifyEmail"))
            verifyEmailService.sendVerificationEmail(user)
        val bio = bioDao.findByUserId(user.id)
        return userMapper.asUserResponse(user, degreeDao.findByUserId(user.id), bio)
    }

    /**
     * Performs the login operation for a user based on the provided [request].
     *
     * @param request The [LoginRequest] containing the email, password and device fingerprint of the user.
     * @return A [Pair] containing a [TokenResponse] with the access token
     * and a [String] representing the refresh token.
     * @throws ApiError if the email or password are invalid.
     */
    @Modifying
    override fun login(request: LoginRequest): Pair<AccessTokenResponse, String> {
        validate(request)
        val user = userService.findUserEntityByEmail(request.email)
        checkCredentialsOrThrow(user, request.password)
        return tokenManager.generateTokenPair(user, request.fingerprint, request.client)
    }

    /**
     * Refreshes an authentication token based on the provided [fingerprint].
     *
     * @param fingerprint The [RefreshRequest] containing the actual refresh token and the device fingerprint.
     * @return A pair of [TokenResponse] with access token and refresh token as a string.
     */
    @Modifying
    override fun refresh(fingerprint: RefreshRequest, refreshToken: String): Pair<AccessTokenResponse, String> {
        return tokenManager.refreshTokenPair(fingerprint, refreshToken)
    }

    /**
     * Revokes the user's refresh token and device fingerprint and logs the user out.
     *
     * @param refreshToken The refresh token to be revoked.
     */
    @Modifying
    override fun logout(refreshToken: String) {
        tokenManager.deleteRefreshToken(refreshToken)
    }

    /**
     * Sends an email to reset the password of a user.
     *
     * @param request The [SendEmailToResetPasswordRequest] containing the email of the user to reset the password.
     * @throws ApiError with [HttpStatus.FORBIDDEN] if the email is not verified.
     */
    @Modifying
    override fun sendEmailToResetPassword(request: SendEmailToResetPasswordRequest) {
        val user = userService.findUserEntityByEmail(request.email)
        if (!verifyEmailService.emailIsVerified(user))
            throw ApiError(HttpStatus.FORBIDDEN, i18n("exception.auth.emailNotVerified"))
        if (resetPasswordTokenDao.existsById(user.id)) resetPasswordTokenDao.deleteById(user.id)
        val token = resetPasswordService.createNewToken(user)
        val link = resetPasswordService.getLinkToResetPassword(token)
        rabbitService.sendMessage(ResetPasswordMail(user.email, link))
    }

    /**
     * Resets the password for a user.
     *
     * @param request The [ResetPasswordRequest] containing the user's new password and reset password token.
     * @return A [Pair] of [TokenResponse] and user ID as string.
     * @throws ApiError with status code NOT_FOUND if the reset password token is not found.
     * @throws ApiError with status code FORBIDDEN if the reset password token is invalid.
     */
    @Modifying
    @Transactional
    override fun resetRefreshPassword(request: ResetPasswordRequest): Pair<AccessTokenResponse, String> {
        val (user, token) = validateResetPasswordRequest(request)
        user.hash = passwordEncoder.encode(request.password)
        resetPasswordTokenDao.delete(token)
        return tokenManager.generateTokenPair(user, request)
    }

    /**
     * Changes the password of a user with the provided email and old password to the new password.
     *
     * @param request A [RefreshPasswordByEmailRequest] object containing the email, old password, and new password.
     * @throws ApiError with [HttpStatus.UNAUTHORIZED] if the old password provided in the request is incorrect.
     * TODO: is this really needed?
     */
    @Modifying
    override fun refreshPassword(request: RefreshPasswordByEmailRequest) {
        val user = userService.findUserEntityByEmail(request.email)
        if (!passwordEncoder.matches(request.oldPassword, user.hash))
            throw ApiError(HttpStatus.UNAUTHORIZED, i18n("exception.auth.wrongPassword"))
        if (passwordEncoder.matches(request.password, user.hash))
            throw ApiError(HttpStatus.CONFLICT, i18n("exception.auth.samePassword"))
        user.hash = passwordEncoder.encode(request.password)
    }


    /**
     * Changes the password of a user with the provided id and old password to the new password.
     *
     * @param request A [RefreshPasswordRequest] object containing old password and new password.
     * @throws ApiError with [HttpStatus.UNAUTHORIZED] if the old password provided in the request is incorrect.
     */
    override fun refreshPassword(userId: Long, request: RefreshPasswordRequest) {
        val user = userService.findUserEntityById(userId)
        if (!passwordEncoder.matches(request.oldPassword, user.hash))
            throw ApiError(HttpStatus.UNAUTHORIZED, i18n("exception.auth.wrongPassword"))
        if (passwordEncoder.matches(request.password, user.hash))
            throw ApiError(HttpStatus.CONFLICT, i18n("exception.auth.samePassword"))
        user.hash = passwordEncoder.encode(request.password)
    }

    /**
     * Validates the user registration request.
     *
     * @param request The [RegistrationRequest] to validate.
     * @throws ApiError if the request is invalid or registration is disabled.
     */
    @Modifying
    private fun validateRegistration(request: RegistrationRequest) {
        if (!featureToggleService.isFeatureEnabled("registration"))
            throw ApiError(HttpStatus.FORBIDDEN, i18n("exception.auth.registrationDisabled"))
        deleteExpiredVerification(request.email)
    }

    /**
     * Validate a [ResetPasswordRequest] token.
     *
     * @param request The [ResetPasswordRequest] that includes the token to validate.
     * @return A [Pair] of [User] and [ResetPasswordToken] if the token is valid.
     * @throws ApiError with [HttpStatus.NOT_FOUND] if the reset password token does not exist.
     * @throws ApiError with [HttpStatus.FORBIDDEN] if the reset password token is invalid or expired.
     */
    private fun validateResetPasswordRequest(request: ResetPasswordRequest): Pair<User, ResetPasswordToken> {
        val token = resetPasswordTokenDao.findByToken(request.token).orElseThrow {
            ApiError(HttpStatus.NOT_FOUND, i18n("exception.notFound.resetPasswordToken", request.token))
        }
        if (request.token != token.token)
            throw ApiError(HttpStatus.FORBIDDEN, i18n("exception.auth.resetTokenInvalid", request.token))
        if (token.createdAt.isBefore(LocalDateTime.now().minusMinutes(15)))
            throw ApiError(HttpStatus.FORBIDDEN, i18n("exception.auth.resetTokenExpired"))
        if (passwordEncoder.matches(request.password, token.user.hash))
            throw ApiError(HttpStatus.CONFLICT, i18n("exception.auth.samePassword"))
        return token.user to token
    }

    /**
     * Deletes an expired email verification for a given email address.
     *
     * @param email The email address for which to delete the verification.
     * @throws ApiError if the email address does not have an expired verification.
     */
    @Modifying
    private fun deleteExpiredVerification(email: String) {
        val userId = userDao.findByEmail(email).orElse(null)?.id ?: return
        if (!verifyEmailService.emailVerificationIsExpired(userId))
            throw ApiError(HttpStatus.CONFLICT, i18n("exception.alreadyExists.email", email))
        userService.delete(userId)
    }

    /**
     * Checks the user's credentials and throws an error if the provided password doesn't match the stored password
     * hash or the email is not verified.
     *
     * @param user The user whose credentials will be checked.
     * @param password The password to check against the stored password hash.
     * @throws ApiError [HttpStatus.UNAUTHORIZED] if the password does not match the stored hash.
     * @throws ApiError [HttpStatus.FORBIDDEN] if the email is not verified.
     */
    private fun checkCredentialsOrThrow(user: User, password: String) {
        if (!passwordEncoder.matches(password, user.hash))
            throw ApiError(HttpStatus.UNAUTHORIZED, i18n("exception.auth.wrongPassword"))
        if (!verifyEmailService.emailIsVerified(user))
            throw ApiError(HttpStatus.FORBIDDEN, i18n("exception.auth.emailNotVerified"))
    }

    /**
     * Initializes default preferences for the given user. If the referral token is not null and exists in the system,
     * referral is processed and user is associated with the referrer.
     *
     * @param user The [User] object to initialize default preferences for.
     * @param referralToken The referral token that is used to process referral.
     */
    @Modifying
    private fun initialUserSetup(user: User, referralToken: String? = null) {
        userPreferencesService.setDefaultPreferences(user)
        if (referralToken != null && affiliateService.tokenIsExist(referralToken)) {
            referralUserService.save(user, referralToken)
            if (!featureToggleService.isFeatureEnabled("verifyEmail"))
                referralUserService.updateReferralStatus(user.id, ReferralStatus.ACTIVE)
        }
    }
}
