package ru.mephi.alumniclub.app.model.dto.news

import javax.validation.constraints.Size

class CarouselRequest(
    @field:Size(max = 60)
    val title: String,
    @field:Size(max = 120)
    val link: String? = null,
)