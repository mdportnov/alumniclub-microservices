package ru.mephi.alumniclub.app.model.dto.atom.request

import org.hibernate.validator.constraints.Range
import javax.validation.constraints.Size

class SendAtomsRequest(
    val userId: Long,
    val sign: Boolean,
    @field:Range(min = 1, max = 3000)
    val amount: Int,
    @field:Size(max = 120)
    val description: String
)