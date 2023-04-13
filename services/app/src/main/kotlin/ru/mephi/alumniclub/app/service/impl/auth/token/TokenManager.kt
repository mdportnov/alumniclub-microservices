package ru.mephi.alumniclub.app.service.impl.auth.token

import org.springframework.data.jpa.repository.Modifying
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import ru.mephi.alumniclub.app.database.entity.user.Fingerprint
import ru.mephi.alumniclub.app.database.entity.user.RefreshToken
import ru.mephi.alumniclub.app.database.entity.user.User
import ru.mephi.alumniclub.app.database.repository.user.FingerprintDao
import ru.mephi.alumniclub.app.database.repository.user.RefreshTokenDao
import ru.mephi.alumniclub.app.model.dto.auth.response.AccessTokenResponse
import ru.mephi.alumniclub.app.model.dto.auth.ClientDeviceDTO
import ru.mephi.alumniclub.app.model.dto.auth.request.RefreshRequest
import ru.mephi.alumniclub.app.model.dto.auth.password.request.ResetPasswordRequest
import ru.mephi.alumniclub.app.model.exception.common.ResourceNotFoundException
import ru.mephi.alumniclub.app.model.mapper.user.FingerprintMapper
import ru.mephi.alumniclub.shared.dto.common.ApiError
import ru.mephi.alumniclub.shared.model.exceptions.common.CorruptedTokenException
import ru.mephi.alumniclub.shared.util.response.ResponseManager
import java.time.Instant
import java.util.*
import javax.transaction.Transactional

@Component
@Transactional
class TokenManager(
    private val fingerprintDao: FingerprintDao,
    private val refreshTokenDao: RefreshTokenDao,
    private val helper: TokenHelper,
    private val fingerprintMapper: FingerprintMapper
) : ResponseManager() {
    fun createAuthToken(header: String) = helper.createAuthToken(header)

    fun findFingerprintByUserAndDeviceId(user: User, deviceId: String): Fingerprint {
        return fingerprintDao.findByDeviceIdAndUser(deviceId, user).orElseThrow {
            ResourceNotFoundException(type = Fingerprint::class.java)
        }
    }

    @Modifying
    fun generateTokenPair(user: User, deviceId: String, client: ClientDeviceDTO): Pair<AccessTokenResponse, String> {
        val refreshToken = createRefreshToken(user, deviceId, client)
        val accessToken = helper.generateAccessToken(user)
        return Pair(AccessTokenResponse(accessToken), refreshToken)
    }

    @Modifying
    fun refreshTokenPair(request: RefreshRequest, refreshToken: String): Pair<AccessTokenResponse, String> {
        val storedRefreshToken = refreshTokenDao.findByFingerprintDeviceId(request.fingerprint)
            .orElseThrow { ApiError(HttpStatus.UNAUTHORIZED, i18n("exception.notFound.fingerprint")) }
        if (helper.hash(refreshToken) != storedRefreshToken.hash)
            throw CorruptedTokenException()
        if (storedRefreshToken.expiresAt < Date.from(Instant.now()))
            throw ApiError(HttpStatus.UNAUTHORIZED, i18n("exception.auth.refreshTokenExpired"))
        return generateTokenPair(storedRefreshToken.fingerprint)
    }

    @Modifying
    fun generateTokenPair(user: User, request: ResetPasswordRequest): Pair<AccessTokenResponse, String> {
        fingerprintDao.deleteByDeviceId(request.fingerprint)
        val fingerprint = fingerprintMapper.asEntity(user, request.fingerprint, request.client)
        fingerprintDao.save(fingerprint)
        return generateTokenPair(fingerprint)
    }

    @Modifying
    private fun generateTokenPair(fingerprint: Fingerprint): Pair<AccessTokenResponse, String> {
        val client = fingerprintMapper.asClientDto(fingerprint)
        val refreshToken = createRefreshToken(fingerprint.user, fingerprint.deviceId, client)
        val accessToken = helper.generateAccessToken(fingerprint.user)
        return Pair(AccessTokenResponse(accessToken), refreshToken)
    }

    @Modifying
    fun deleteRefreshToken(refreshToken: String) {
        val tokenEntity = refreshTokenDao.findByHash(helper.hash(refreshToken)) ?: return
        fingerprintDao.deleteByDeviceId(tokenEntity.fingerprint.deviceId)
    }

    @Modifying
    private fun createRefreshToken(user: User, deviceId: String, client: ClientDeviceDTO): String {
        refreshTokenDao.deleteByFingerprintDeviceIdAndFingerprintUser(deviceId, user)
        val newRefreshToken = UUID.randomUUID().toString()
        val tokenEntity = RefreshToken(
            expiresAt = helper.getRefreshTokenExpiration(),
            hash = helper.hash(newRefreshToken),
            fingerprint = createFingerprint(user, deviceId, client),
        )
        refreshTokenDao.save(tokenEntity)
        return newRefreshToken
    }

    @Modifying
    private fun createFingerprint(user: User, deviceId: String, client: ClientDeviceDTO): Fingerprint {
        fingerprintDao.deleteByDeviceId(deviceId)
        val fingerprint = fingerprintMapper.asEntity(user, deviceId, client)
        return fingerprintDao.save(fingerprint)
    }

}

