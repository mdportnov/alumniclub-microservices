package ru.mephi.alumniclub.app.model.dto.atom.response

import ru.mephi.alumniclub.shared.dto.AbstractCreatedAtResponse
import java.time.LocalDateTime
import java.util.*

class AtomHistoryResponse(
    id: UUID,
    createdAt: LocalDateTime,
    val sign: Boolean,
    val amount: Int,
    val description: String,
) : AbstractCreatedAtResponse<UUID>(id, createdAt)