package ru.mephi.alumniclub.app.model.dto.partnership.request

import java.time.LocalDateTime
import javax.validation.constraints.Size

class PartnershipRequest(
    @field:Size(max = 600)
    val tag: String,
    @field:Size(max = 6)
    val color: String,
    @field:Size(max = 300)
    val projectName: String,
    @field:Size(max = 600)
    val aboutProject: String,
    @field:Size(max = 600)
    val helpDescription: String,
    val currentUntil: LocalDateTime,
    @field:Size(max = 600)
    val creatorDescription: String,
    val creatorId: Long,
)