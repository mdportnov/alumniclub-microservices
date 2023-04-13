package ru.mephi.alumniclub.app.service.impl.auth

import org.springframework.stereotype.Service
import ru.mephi.alumniclub.app.config.properties.ServerUrlsProperties
import ru.mephi.alumniclub.app.database.entity.email.ResetPasswordToken
import ru.mephi.alumniclub.app.database.entity.user.User
import ru.mephi.alumniclub.app.database.repository.email.ResetPasswordTokenDao
import ru.mephi.alumniclub.app.service.ResetPasswordService
import java.time.LocalDateTime
import java.util.*

@Service
class ResetPasswordServiceImpl(
    private val tokenDao: ResetPasswordTokenDao,
    private val resetPasswordTokenDao: ResetPasswordTokenDao,
    private val serverUrlsProperties: ServerUrlsProperties
) : ResetPasswordService {

    /**
     * Creates a new [ResetPasswordToken] for a given [User].
     *
     * @param user The [User] for whom to create a new reset password token.
     * @return The newly created [ResetPasswordToken].
     */
    override fun createNewToken(user: User): ResetPasswordToken {
        val token = ResetPasswordToken(user, UUID.randomUUID().toString())
        return tokenDao.save(token)
    }

    /**
     * Returns a URL to reset password using a [ResetPasswordToken].
     *
     * @param token The [ResetPasswordToken] used to generate the reset password link.
     * @return A URL to reset password using the provided [ResetPasswordToken].
     */
    override fun getLinkToResetPassword(token: ResetPasswordToken): String {
        return "${serverUrlsProperties.fullBaseUrl}/auth/password/reset/refresh?token=${token.token}"
    }

    /**
     * Returns whether a given [token] exists and is still valid.
     *
     * @param token The [token] to check.
     * @return `true` if the [token] exists and is still valid, `false` otherwise.
     */
    override fun tokenIsExist(token: String): Boolean {
        val entity = resetPasswordTokenDao.findByToken(token).orElse(null) ?: return false
        return entity.createdAt.isAfter(LocalDateTime.now().minusMinutes(15))
    }
}
