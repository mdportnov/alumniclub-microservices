package ru.mephi.alumniclub.app.model.dto.form.response

import ru.mephi.alumniclub.app.model.dto.form.FieldNameAndValue
import ru.mephi.alumniclub.app.model.dto.user.response.UserShortResponse
import ru.mephi.alumniclub.app.model.enumeration.FormStatus
import ru.mephi.alumniclub.app.model.enumeration.FormType
import ru.mephi.alumniclub.shared.dto.AbstractCreatedAtResponse
import java.time.LocalDateTime

class FormResponse(
    id: Long,
    createdAt: LocalDateTime,
    val author: UserShortResponse,
    val status: FormStatus,
    val type: FormType,
    val answers: List<FieldNameAndValue> = listOf()
) : AbstractCreatedAtResponse<Long>(id, createdAt)