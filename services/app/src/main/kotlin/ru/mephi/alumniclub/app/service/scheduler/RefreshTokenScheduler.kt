package ru.mephi.alumniclub.app.service.scheduler

import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import ru.mephi.alumniclub.app.database.repository.user.FingerprintDao
import ru.mephi.alumniclub.app.database.repository.user.RefreshTokenDao
import ru.mephi.alumniclub.shared.logging.AlumniLogger
import java.time.Instant
import java.util.*

@Service
class RefreshTokenScheduler(
    private val refreshTokenDao: RefreshTokenDao,
    private val fingerprintDao: FingerprintDao,
    private val logger: AlumniLogger
) {
    @Scheduled(fixedDelay = 24 * 3600 * 1000L) // 1 day
    fun handleNotifications() {
        logger.info("Start deleting expired refresh tokens and fingerprints")
        val tokens = refreshTokenDao.findByExpiresAtBefore(Date.from(Instant.now()))
        tokens.forEach { token -> fingerprintDao.delete(token.fingerprint) }
        logger.info("Deleting expired refresh tokens and fingerprints has been completed")
    }
}