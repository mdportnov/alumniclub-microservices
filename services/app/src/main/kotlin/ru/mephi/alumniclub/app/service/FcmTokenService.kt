package ru.mephi.alumniclub.app.service

import org.springframework.web.bind.annotation.RequestBody
import ru.mephi.alumniclub.app.model.dto.fcm.request.RemoveFcmTokenRequest
import ru.mephi.alumniclub.app.model.dto.fcm.request.UploadTokenRequest

interface FcmTokenService {
    fun uploadToken(@RequestBody request: UploadTokenRequest, userId: Long)
    fun removeToken(@RequestBody request: RemoveFcmTokenRequest, userId: Long)
}
