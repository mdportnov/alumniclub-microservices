package ru.mephi.alumniclub.app.model.dto

import ru.mephi.alumniclub.shared.dto.AbstractCreatedAtResponse

open class CursorResponse<T : AbstractCreatedAtResponse<*>>(
    content: List<T>,
    val numberOfElements: Long,
) {
    val content = changePhotosPath(content)
    val first = content.firstOrNull()?.createdAt
    val last = content.lastOrNull()?.createdAt
}
