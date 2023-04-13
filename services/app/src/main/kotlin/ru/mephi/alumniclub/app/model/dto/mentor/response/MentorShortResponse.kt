package ru.mephi.alumniclub.app.model.dto.mentor.response

import ru.mephi.alumniclub.shared.dto.AbstractResponse

class MentorShortResponse(
    name: String,
    surname: String,
    userId: Long,
    val company: String?,
    val position: String?,
    val photoPath: String?,
    var graduation: String?,
    val available: Boolean,
    val tags: String?,
    val interviewLink: String?
) : AbstractResponse<Long>(userId) {
    val fullName = "$name $surname"
}
