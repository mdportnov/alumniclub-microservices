package ru.mephi.alumniclub.app.model.dto.survey.response

import java.util.*

class SurveyAnswersInfoResponse(
    val surveyId: UUID,
    val allVotes: Long,
    val info: List<ItemAnswerInfoResponse>
)
