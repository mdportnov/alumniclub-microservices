package ru.mephi.alumniclub.app.model.dto.mentor.response

import ru.mephi.alumniclub.shared.dto.AbstractResponse
import ru.mephi.alumniclub.shared.dto.photo.PhotoPathed

class MentorFullResponse(
    userId: Long,
    name: String,
    surname: String,
    patronymic: String? = null,
    val company: String? = null,
    val position: String? = null,
    var expertArea: String? = null,
    var whyAreYouMentor: String? = null,
    var graduation: String? = null,
    var formatsOfInteractions: String? = null,
    var interviewLink: String? = null,
    var tags: String? = null,
    override var photoPath: String?,
    val available: Boolean,
) : AbstractResponse<Long>(userId), PhotoPathed {
    val fullName = "$surname $name" + if (patronymic != null) " $patronymic" else ""
}
