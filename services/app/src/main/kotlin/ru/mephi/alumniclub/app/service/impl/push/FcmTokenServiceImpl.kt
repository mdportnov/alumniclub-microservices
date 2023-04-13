package ru.mephi.alumniclub.app.service.impl.push

import org.springframework.stereotype.Service
import ru.mephi.alumniclub.app.database.entity.fcm.FcmToken
import ru.mephi.alumniclub.app.database.entity.user.Fingerprint
import ru.mephi.alumniclub.app.database.repository.fcm.FcmTokenDao
import ru.mephi.alumniclub.app.model.dto.fcm.request.RemoveFcmTokenRequest
import ru.mephi.alumniclub.app.model.dto.fcm.request.UploadTokenRequest
import ru.mephi.alumniclub.app.model.exception.common.ResourceNotFoundException
import ru.mephi.alumniclub.app.model.mapper.fcm.FcmTokenMapper
import ru.mephi.alumniclub.app.service.FcmTokenService
import ru.mephi.alumniclub.app.service.UserService
import ru.mephi.alumniclub.app.service.impl.auth.token.TokenManager
import ru.mephi.alumniclub.shared.util.response.ResponseManager
import javax.transaction.Transactional

@Service
class FcmTokenServiceImpl(
    private val repository: FcmTokenDao,
    private val mapper: FcmTokenMapper,
    private val userService: UserService,
    private val tokenManager: TokenManager
) : ResponseManager(), FcmTokenService {
    @Transactional
    override fun uploadToken(request: UploadTokenRequest, userId: Long) {
        val user = userService.findUserEntityById(userId)
        val fingerprint = tokenManager.findFingerprintByUserAndDeviceId(user, request.fingerprint)
        val entity = mapper.asEntity(fingerprint, request)
        repository.save(entity)
    }

    @Transactional
    override fun removeToken(request: RemoveFcmTokenRequest, userId: Long) {
        val user = userService.findUserEntityById(userId)
        val fingerprint = tokenManager.findFingerprintByUserAndDeviceId(user, request.fingerprint)
        val entity = findTokenByFingerprint(fingerprint)
        repository.delete(entity)
    }

    private fun findTokenByFingerprint(fingerprint: Fingerprint): FcmToken {
        return repository.findByFingerprint(fingerprint).orElseThrow {
            ResourceNotFoundException(type = FcmToken::class.java)
        }
    }
}
