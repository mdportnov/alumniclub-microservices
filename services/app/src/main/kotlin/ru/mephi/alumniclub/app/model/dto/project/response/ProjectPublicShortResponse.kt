package ru.mephi.alumniclub.app.model.dto.project.response

import ru.mephi.alumniclub.shared.dto.AbstractCreatedAtResponse
import java.time.LocalDateTime

class ProjectPublicShortResponse(
    id: Long,
    createdAt: LocalDateTime,
    val name: String,
    var membersCount: Long,
    val color: String? = null
) : AbstractCreatedAtResponse<Long>(id, createdAt)
