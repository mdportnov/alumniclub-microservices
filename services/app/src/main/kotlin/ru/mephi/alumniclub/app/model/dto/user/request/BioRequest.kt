package ru.mephi.alumniclub.app.model.dto.user.request

import ru.mephi.alumniclub.shared.util.constants.userDataLength
import javax.validation.constraints.Size

class BioRequest(
    @field:Size(max = userDataLength)
    val country: String? = null,

    @field:Size(max = userDataLength)
    val city: String? = null,

    @field:Size(max = userDataLength)
    val jobArea: String? = null,

    @field:Size(max = userDataLength)
    val company: String? = null,

    @field:Size(max = userDataLength)
    val job: String? = null,

    @field:Size(max = userDataLength)
    val experience: String? = null,

    @field:Size(max = userDataLength)
    val hobbies: String? = null,
)
