package ru.mephi.alumniclub.app.model.dto.feed.response

import ru.mephi.alumniclub.app.model.dto.user.response.UserShortResponse
import ru.mephi.alumniclub.shared.dto.AbstractCreatedAtResponse
import java.time.LocalDateTime

class LikeResponse(
    id: Long,
    createdAt: LocalDateTime,
    val user: UserShortResponse
) : AbstractCreatedAtResponse<Long>(id, createdAt)
