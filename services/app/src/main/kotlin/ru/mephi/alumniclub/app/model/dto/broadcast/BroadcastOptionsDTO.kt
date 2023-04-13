package ru.mephi.alumniclub.app.model.dto.broadcast

class BroadcastOptionsDTO(
    val sendPush: Boolean = false,
    var sendEmail: Boolean = false,
    val sendNotification: Boolean = true,
    val ignorePreferences: Boolean = false
)