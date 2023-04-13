package ru.mephi.alumniclub.app.model.dto.form.response

import ru.mephi.alumniclub.app.model.enumeration.FormType
import ru.mephi.alumniclub.shared.dto.AbstractCreatedAtResponse
import java.time.LocalDateTime

class SelfFormResponse(
    id: Long,
    createdAt: LocalDateTime,
    val type: FormType
) : AbstractCreatedAtResponse<Long>(id, createdAt)
