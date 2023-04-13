package ru.mephi.alumniclub.app.model.dto.user

import ru.mephi.alumniclub.app.model.enumeration.user.DegreeType
import ru.mephi.alumniclub.shared.util.constants.userDataLength
import javax.validation.constraints.Min
import javax.validation.constraints.Size

class DegreeDTO(
    @field:Min(1900)
    val enrollment: Int,
    val graduation: Int?,
    val degree: DegreeType,
    @field:Size(max = userDataLength)
    val description: String
)
