package ru.mephi.alumniclub.app.model.dto.atom.request

import org.hibernate.validator.constraints.Range
import javax.validation.constraints.Size

class MerchRequest(
    @field:Size(max = 60)
    val name: String,
    @field:Size(max = 600)
    val description: String,
    @field:Range(min = 0, max = 3000)
    val cost: Int,
    val availability: Boolean = true,
)