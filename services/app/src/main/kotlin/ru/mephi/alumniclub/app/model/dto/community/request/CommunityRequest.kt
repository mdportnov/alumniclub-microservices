package ru.mephi.alumniclub.app.model.dto.community.request

import javax.validation.constraints.Size

class CommunityRequest(
    @Size(max = 60)
    val name: String,
    val hidden: Boolean = false
)
