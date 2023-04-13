package ru.mephi.alumniclub.app.service

import ru.mephi.alumniclub.app.model.dto.broadcast.request.BroadcastByPublicationRequest
import ru.mephi.alumniclub.app.model.dto.broadcast.request.BroadcastRequest
import java.util.*

interface NotificationSenderService {
    fun sendSimpleTextNotification(
        request: BroadcastRequest,
        ignorePreferences: Boolean = false,
        extraData: Map<String, String> = mutableMapOf(),
        contentId: UUID? = null
    )

    fun sendSimpleTextNotification(request: BroadcastByPublicationRequest, ignorePreferences: Boolean = false)
}
