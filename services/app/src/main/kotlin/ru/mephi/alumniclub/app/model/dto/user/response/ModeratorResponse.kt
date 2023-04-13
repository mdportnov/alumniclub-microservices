package ru.mephi.alumniclub.app.model.dto.user.response

import ru.mephi.alumniclub.shared.dto.AbstractCreatedAtResponse
import ru.mephi.alumniclub.shared.dto.photo.PhotoPathed
import java.time.LocalDateTime

class ModeratorResponse(
    id: Long,
    createdAt: LocalDateTime,
    val name: String,
    val surname: String,
    val patronymic: String?,
    val permissions: PermissionsResponse?,
    override var photoPath: String?,
) : AbstractCreatedAtResponse<Long>(id, createdAt), PhotoPathed
