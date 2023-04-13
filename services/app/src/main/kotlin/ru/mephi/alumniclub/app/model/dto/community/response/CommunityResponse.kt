package ru.mephi.alumniclub.app.model.dto.community.response

import ru.mephi.alumniclub.shared.dto.AbstractCreatedAtResponse
import ru.mephi.alumniclub.shared.dto.photo.PhotoPathed
import java.time.LocalDateTime

class CommunityResponse(
    id: Long,
    createdAt: LocalDateTime,
    override var photoPath: String?,
    val name: String,
    val membersCount: Int,
    val hidden: Boolean,
    val projectId: Long?,
    val role: Boolean,
) : AbstractCreatedAtResponse<Long>(id, createdAt), PhotoPathed
