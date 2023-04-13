package ru.mephi.alumniclub.app.model.dto.broadcast.response

import ru.mephi.alumniclub.app.model.dto.user.response.UserShortResponse
import ru.mephi.alumniclub.shared.dto.photo.PhotoPathed
import java.util.*

class BroadcastNotificationShortResponse(
    val id: UUID,
    val title: String,
    val content: String,
    override var photoPath: String?,
    val author: UserShortResponse?,
) : PhotoPathed