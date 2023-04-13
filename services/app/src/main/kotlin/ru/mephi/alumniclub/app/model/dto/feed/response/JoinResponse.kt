package ru.mephi.alumniclub.app.model.dto.feed.response

import ru.mephi.alumniclub.app.model.dto.user.response.UserPreviewResponse
import ru.mephi.alumniclub.shared.dto.AbstractCreatedAtResponse
import java.time.LocalDateTime

class JoinResponse(
    id: Long,
    createdAt: LocalDateTime,
    val user: UserPreviewResponse
) : AbstractCreatedAtResponse<Long>(id, createdAt)
