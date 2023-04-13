package ru.mephi.alumniclub.app.model.mapper.user

import org.springframework.stereotype.Component
import ru.mephi.alumniclub.app.database.entity.user.Fingerprint
import ru.mephi.alumniclub.app.database.entity.user.User
import ru.mephi.alumniclub.app.model.dto.auth.ClientDeviceDTO

@Component
class FingerprintMapper {
    fun asEntity(user: User, deviceId: String, client: ClientDeviceDTO): Fingerprint {
        return Fingerprint(
            deviceId = deviceId,
            name = client.name,
            version = client.version,
            type = client.type
        ).apply { this.user = user }
    }

    fun asClientDto(fingerprint: Fingerprint): ClientDeviceDTO {
        return ClientDeviceDTO(
            name = fingerprint.name,
            version = fingerprint.version,
            type = fingerprint.type
        )
    }

    fun asListResponse(fingerprints: List<Fingerprint>) = fingerprints.map(::asClientDto)
}