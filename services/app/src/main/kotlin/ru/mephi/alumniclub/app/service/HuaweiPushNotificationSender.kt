package ru.mephi.alumniclub.app.service

import ru.mephi.alumniclub.app.model.dto.broadcast.request.BroadcastRequest
import ru.mephi.push_sdk.response.SendResponse

interface HuaweiPushNotificationSender {
    fun sendToUsers(
        push: BroadcastRequest,
        tokens: List<String>,
        extraData: Map<String, String> = mutableMapOf()
    ): List<SendResponse>
}