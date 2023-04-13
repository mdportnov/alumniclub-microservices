package ru.mephi.alumniclub.app.service

import ru.mephi.alumniclub.app.model.dto.broadcast.request.BroadcastByPublicationRequest

interface EmailService {
    fun sendPublicationContentMail(request: BroadcastByPublicationRequest, ignorePreferences: Boolean = false)
    fun sendBroadcastMail(request: BroadcastByPublicationRequest, ignorePreferences: Boolean = false)
}
