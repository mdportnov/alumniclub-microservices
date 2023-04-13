package ru.mephi.alumniclub.app.model.dto.broadcast.request

import javax.validation.constraints.Size

class BroadcastNotificationRequest(
    @field:Size(max = 300)
    val title: String,
    @field:Size(max = 600)
    val content: String,
)