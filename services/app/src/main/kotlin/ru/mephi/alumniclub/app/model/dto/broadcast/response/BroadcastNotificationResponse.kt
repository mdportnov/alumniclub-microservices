package ru.mephi.alumniclub.app.model.dto.broadcast.response

import ru.mephi.alumniclub.app.model.dto.user.response.UserShortResponse
import ru.mephi.alumniclub.shared.dto.AbstractCreatedAtResponse
import ru.mephi.alumniclub.shared.dto.photo.PhotoPathed
import java.time.LocalDateTime
import java.util.*

class BroadcastNotificationResponse(
    id: UUID,
    createdAt: LocalDateTime,
    val title: String,
    val content: String,
    override var photoPath: String?,
    val author: UserShortResponse?,
) : AbstractCreatedAtResponse<UUID>(id, createdAt), PhotoPathed
