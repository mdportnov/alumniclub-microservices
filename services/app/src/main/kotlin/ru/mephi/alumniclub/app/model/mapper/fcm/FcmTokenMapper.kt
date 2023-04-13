package ru.mephi.alumniclub.app.model.mapper.fcm

import org.springframework.stereotype.Component
import ru.mephi.alumniclub.app.database.entity.fcm.FcmToken
import ru.mephi.alumniclub.app.database.entity.user.Fingerprint
import ru.mephi.alumniclub.app.database.entity.user.User
import ru.mephi.alumniclub.app.model.dto.fcm.request.UploadTokenRequest

@Component
class FcmTokenMapper {

    /**
     * Transforms the [UploadTokenRequest] object and the [User] into a [FcmToken] object.
     *
     * @param request The [UploadTokenRequest] object to be transformed.
     * @param user The user to be used in the transformation.
     * @return The resulting [FcmToken] object.
     */

    fun asEntity(fingerprint: Fingerprint, request: UploadTokenRequest): FcmToken {
        return FcmToken(
            fingerprint = fingerprint,
            token = request.token,
            isValid = true
        )
    }
}
