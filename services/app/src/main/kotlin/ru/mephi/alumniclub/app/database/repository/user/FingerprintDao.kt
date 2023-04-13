package ru.mephi.alumniclub.app.database.repository.user


import ru.mephi.alumniclub.app.database.entity.user.Fingerprint
import ru.mephi.alumniclub.app.database.entity.user.User
import ru.mephi.alumniclub.shared.database.repository.AbstractRepository
import java.util.Optional

interface FingerprintDao : AbstractRepository<Fingerprint> {
    fun findByDeviceId(deviceId: String): Fingerprint?
    fun findByDeviceIdAndUser(deviceId: String, user: User): Optional<Fingerprint>
    fun findByUserId(userId: Long): List<Fingerprint>
    fun findByUserIdAndDeviceIdNot(id: Long, deviceId: String): List<Fingerprint>
    fun deleteByDeviceId(deviceId: String)
    fun deleteByUserId(userId: Long)
}
