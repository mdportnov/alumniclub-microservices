package ru.mephi.alumniclub.app.model.dto.mentor.request

import ru.mephi.alumniclub.shared.util.constants.mediumLength
import ru.mephi.alumniclub.shared.util.constants.userDataLength
import javax.validation.constraints.Size

class UpdateMentorRequest(
    @field:Size(max = userDataLength)
    var company: String,
    @field:Size(max = userDataLength)
    var position: String,
    @field:Size(max = userDataLength)
    var expertArea: String,
    @field:Size(max = userDataLength)
    var whyAreYouMentor: String,
    @field:Size(max = userDataLength)
    var graduation: String,
    @field:Size(max = userDataLength)
    var formatsOfInteractions: String,
    @field:Size(max = userDataLength)
    var tags: String,
    @field:Size(max = mediumLength)
    var interviewLink: String?,
)