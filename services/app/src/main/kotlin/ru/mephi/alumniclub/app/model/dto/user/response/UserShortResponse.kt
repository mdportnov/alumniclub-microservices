package ru.mephi.alumniclub.app.model.dto.user.response

import ru.mephi.alumniclub.shared.dto.AbstractResponse
import ru.mephi.alumniclub.shared.dto.photo.PhotoPathed

class UserShortResponse(
    id: Long,
    val fullName: String,
    override var photoPath: String?,
) : AbstractResponse<Long>(id), PhotoPathed