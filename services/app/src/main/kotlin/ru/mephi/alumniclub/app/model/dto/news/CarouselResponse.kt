package ru.mephi.alumniclub.app.model.dto.news

import ru.mephi.alumniclub.shared.dto.AbstractCreatedAtResponse
import ru.mephi.alumniclub.shared.dto.photo.PhotoPathed
import java.time.LocalDateTime

class CarouselResponse(
    id: Long,
    createdAt: LocalDateTime,
    val title: String,
    val link: String? = null,
    override var photoPath: String? = null,
) : AbstractCreatedAtResponse<Long>(id, createdAt), PhotoPathed