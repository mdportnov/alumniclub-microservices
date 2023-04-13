package ru.mephi.alumniclub.app.model.dto.partnership.response

import ru.mephi.alumniclub.shared.dto.AbstractCreatedAtResponse
import ru.mephi.alumniclub.shared.dto.photo.PhotoPathed
import java.time.LocalDateTime

class PartnershipResponse(
    id: Long,
    createdAt: LocalDateTime,
    val tag: String,
    val color: String,
    val projectName: String,
    val aboutProject: String,
    val helpDescription: String,
    val currentUntil: LocalDateTime,
    val creator: CreatorResponse,

    override var photoPath: String?
) : AbstractCreatedAtResponse<Long>(id, createdAt), PhotoPathed
