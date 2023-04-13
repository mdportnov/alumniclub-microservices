package ru.mephi.alumniclub.app.database.repository.user


import org.springframework.data.jpa.repository.Modifying
import ru.mephi.alumniclub.app.database.entity.user.RefreshToken
import ru.mephi.alumniclub.app.database.entity.user.User
import ru.mephi.alumniclub.shared.database.repository.AbstractRepository
import java.util.*

interface RefreshTokenDao : AbstractRepository<RefreshToken> {
    fun findByFingerprintDeviceId(deviceId: String): Optional<RefreshToken>

    fun findByExpiresAtBefore(expiresAt: Date): Iterable<RefreshToken>

    @Modifying
    fun deleteByFingerprintDeviceIdAndFingerprintUser(deviceId: String, user: User)

    fun findByHash(hash: String): RefreshToken?
}
