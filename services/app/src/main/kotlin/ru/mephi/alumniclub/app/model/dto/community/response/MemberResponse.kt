package ru.mephi.alumniclub.app.model.dto.community.response

import ru.mephi.alumniclub.app.model.dto.user.response.UserPreviewResponse
import ru.mephi.alumniclub.shared.dto.AbstractCreatedAtResponse
import java.time.LocalDateTime

class MemberResponse(
    id: Long,
    createdAt: LocalDateTime,
    val mentor: Boolean,
    val user: UserPreviewResponse
) : AbstractCreatedAtResponse<Long>(id, createdAt)
