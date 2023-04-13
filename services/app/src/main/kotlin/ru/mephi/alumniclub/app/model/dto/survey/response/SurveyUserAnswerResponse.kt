package ru.mephi.alumniclub.app.model.dto.survey.response

import ru.mephi.alumniclub.app.database.entity.survey.SurveyType
import ru.mephi.alumniclub.app.model.dto.user.response.UserShortResponse
import ru.mephi.alumniclub.shared.dto.AbstractResponse
import java.util.*

class SurveyUserAnswerResponse(
    id: Long,
    val surveyId: UUID,
    val surveyType: SurveyType,
    val user: UserShortResponse,
    val selectedAnswers: List<SurveyItemAnswerResponse> = mutableListOf(),
    var freeAnswer: String? = null
) : AbstractResponse<Long>(id)
