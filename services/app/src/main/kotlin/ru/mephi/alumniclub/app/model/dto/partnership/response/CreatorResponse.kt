package ru.mephi.alumniclub.app.model.dto.partnership.response

import ru.mephi.alumniclub.shared.dto.AbstractCreatedAtResponse
import ru.mephi.alumniclub.shared.dto.photo.PhotoPathed
import java.time.LocalDateTime

class CreatorResponse(
    id: Long,
    createdAt: LocalDateTime,
    val name: String,
    val surname: String,
    val creatorDescription: String,
    override var photoPath: String?,
) : AbstractCreatedAtResponse<Long>(id, createdAt), PhotoPathed
