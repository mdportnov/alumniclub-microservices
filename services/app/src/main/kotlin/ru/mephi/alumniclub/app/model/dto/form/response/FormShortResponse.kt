package ru.mephi.alumniclub.app.model.dto.form.response

import ru.mephi.alumniclub.app.model.dto.user.response.UserShortResponse
import ru.mephi.alumniclub.app.model.enumeration.FormStatus
import ru.mephi.alumniclub.app.model.enumeration.FormType
import ru.mephi.alumniclub.shared.dto.AbstractCreatedAtResponse
import java.time.LocalDateTime

class FormShortResponse(
    id: Long,
    createdAt: LocalDateTime,
    val author: UserShortResponse,
    val status: FormStatus,
    val type: FormType,
) : AbstractCreatedAtResponse<Long>(id, createdAt)