package ru.mephi.alumniclub.app.model.dto.survey.request

import java.time.LocalDateTime
import javax.validation.constraints.Size

class UpdateSurveyRequest(
    @field:Size(max = 600)
    val title: String,
    @field:Size(max = 60000)
    val content: String,
    var endsAt: LocalDateTime,
)