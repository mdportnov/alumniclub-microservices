package ru.mephi.alumniclub.app.model.dto.survey.response

import ru.mephi.alumniclub.shared.dto.AbstractResponse

class SurveyItemAnswerResponse(
    id: Long,
    var answer: String
) : AbstractResponse<Long>(id)