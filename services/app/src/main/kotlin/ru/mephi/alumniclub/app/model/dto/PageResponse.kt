package ru.mephi.alumniclub.app.model.dto

import ru.mephi.alumniclub.shared.dto.AbstractResponse

class PageResponse<T : AbstractResponse<*>>(
    content: List<T>,
    number: Long,
    numberOfElements: Long,
    val totalPages: Long,
) {
    val content = changePhotosPath(content)
    val page = number + 1
    val size = numberOfElements
}
