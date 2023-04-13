package ru.mephi.alumniclub.app.model.dto.survey.request

import javax.validation.constraints.Size

class VoteSurveyRequest(
    val selectedAnswers: List<Long> = mutableListOf(),
    @field:Size(max = 600)
    val freeAnswer: String? = null
)