package ru.mephi.alumniclub.app.model.dto.project.request

import javax.validation.constraints.Size

open class ProjectRequest(
    @field:Size(max = 60)
    val name: String,
    @field:Size(max = 600)
    val description: String,
    @field:Size(max = 6)
    val color: String? = null,
    val hiddenCommunity: Boolean
)
