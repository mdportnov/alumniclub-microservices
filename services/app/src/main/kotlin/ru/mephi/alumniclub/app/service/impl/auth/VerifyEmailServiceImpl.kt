package ru.mephi.alumniclub.app.service.impl.auth

import org.springframework.beans.factory.annotation.Value
import org.springframework.data.jpa.repository.Modifying
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import ru.mephi.alumniclub.app.config.properties.ServerUrlsProperties
import ru.mephi.alumniclub.app.database.entity.user.User
import ru.mephi.alumniclub.app.database.entity.user.VerifyEmailToken
import ru.mephi.alumniclub.app.database.repository.user.VerifyEmailTokenDao
import ru.mephi.alumniclub.app.model.enumeration.referral.ReferralStatus
import ru.mephi.alumniclub.app.service.*
import ru.mephi.alumniclub.shared.dto.common.ApiError
import ru.mephi.alumniclub.shared.dto.mail.VerifyEmailMail
import ru.mephi.alumniclub.shared.util.response.ResponseManager
import java.time.LocalDateTime

@Service
class VerifyEmailServiceImpl(
    // DAO
    private val dao: VerifyEmailTokenDao,

    // Properties
    private val serverUrlsProperties: ServerUrlsProperties,
    @Value("\${verifyEmail.tokenTtl}")
    private val tokenTtl: Long,

    // Services
    private val referralUserService: ReferralUserService,
    private val rabbitService: RabbitService,
    private val jwtManager: JwtManager

) : ResponseManager(), VerifyEmailService {

    /**
     * Builds a URL link with the provided [token].
     *
     * @param token The token to be appended to the URL.
     * @return A URL link with the provided [token].
     */
    private fun buildLink(token: String): String {
        return "${serverUrlsProperties.fullBaseUrl}/api/v1/public/auth/email/verify?token=${token}"
    }

    /**
     * Finds the [VerifyEmailToken] associated with the given token.
     *
     * @param token The token used to find the [VerifyEmailToken].
     * @throws ApiError with [HttpStatus.NOT_FOUND] if the token is not found.
     * @throws ApiError with [HttpStatus.FORBIDDEN] if the token is expired.
     * @return The [VerifyEmailToken] associated with the given token.
     */
    private fun findVerifyToken(token: String): VerifyEmailToken {
        val entity = dao.findByToken(token)
            .orElseThrow { ApiError(HttpStatus.NOT_FOUND, i18n("exception.auth.emailAlreadyVerified")) }
        if (tokenIsExpired(entity))
            throw ApiError(HttpStatus.FORBIDDEN, i18n("exception.auth.verifyTokenExpired"))
        return entity
    }

    /**
     * Sends an email to the user for email verification.
     *
     * @param user The [User] object to which the verification email will be sent.
     */
    override fun sendVerificationEmail(user: User) {
        val token = jwtManager.getToken(mapOf("userId" to user.id))
        val email = VerifyEmailMail(buildLink(token), tokenTtl, user.email)
        dao.save(VerifyEmailToken(user, token))
        rabbitService.sendMessage(email)
    }

    /**
     * Verifies a user's email by checking if the provided [token] is valid and not expired.
     *
     * @param token The verification token to be checked.
     * @throws ApiError with [HttpStatus.FORBIDDEN] if the token is expired.
     */
    @Modifying
    override fun verifyEmail(token: String) {
        val entity = findVerifyToken(token)
        dao.delete(entity)
        referralUserService.tryUpdateReferralStatus(entity.id, ReferralStatus.ACTIVE)
    }

    /**
     * Determines if the email address associated with a [User] object is verified.
     *
     * @param user The [User] object for which to determine if the email address is verified.
     * @return True if the email address associated with the [User] object exists in verify table,
     * false otherwise.
     */
    override fun emailIsVerified(user: User): Boolean {
        return !dao.existsById(user.id)
    }

    /**
     * Determines if the email verification token associated with a user has expired.
     *
     * @param id The ID of the user for which to determine if the email verification token has expired.
     * @return True if the email verification token has expired, false otherwise.
     * @throws ApiError with [HttpStatus.NOT_FOUND] if the email verification token does not exist.
     */
    override fun emailVerificationIsExpired(id: Long): Boolean {
        val token = dao.findById(id)
            .orElseThrow { ApiError(HttpStatus.NOT_FOUND, i18n("exception.auth.emailAlreadyVerified")) }
        return dao.existsById(id) && tokenIsExpired(token)
    }

    /**
     * Returns a list of all [VerifyEmailToken] objects.
     *
     * @return A list of all [VerifyEmailToken] objects.
     */
    override fun findAllUnVerified(): Iterable<VerifyEmailToken> {
        return dao.findAll()
    }

    /**
     * Determines if a [VerifyEmailToken] object has expired.
     *
     * @param token The [VerifyEmailToken] object for which to determine if it has expired.
     * @return True if the [VerifyEmailToken] object has expired, false otherwise.
     */
    override fun tokenIsExpired(token: VerifyEmailToken): Boolean {
        return token.createdAt.isBefore(LocalDateTime.now().minusMinutes(tokenTtl))
    }
}
