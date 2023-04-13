package ru.mephi.alumniclub.app.model.dto.project.response

import ru.mephi.alumniclub.app.model.enumeration.ProjectType
import ru.mephi.alumniclub.shared.dto.AbstractCreatedAtResponse
import ru.mephi.alumniclub.shared.dto.photo.PhotoPathed
import java.time.LocalDateTime

class ProjectShortResponse(
    id: Long,
    createdAt: LocalDateTime,
    val type: ProjectType,
    val name: String,
    var membersCount: Long,
    var joined: Boolean,
    var archive: Boolean,
    override var photoPath: String?,
    val color: String? = null,
) : AbstractCreatedAtResponse<Long>(id, createdAt), PhotoPathed
