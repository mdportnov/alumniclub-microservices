package ru.mephi.alumniclub.app.model.dto.survey.response

import ru.mephi.alumniclub.app.database.entity.survey.SurveyType
import ru.mephi.alumniclub.app.model.dto.user.response.UserShortResponse
import java.time.LocalDateTime
import java.util.*

class SurveyResponseWithMetadata(
    id: UUID,
    createdAt: LocalDateTime,
    title: String,
    content: String,
    author: UserShortResponse? = null,
    photoPath: String? = null,
    endsAt: LocalDateTime,
    allowCount: Int,
    baseAnswers: MutableList<SurveyItemAnswerResponse> = mutableListOf(),
    type: SurveyType,
    var metadata: SurveyMetadata? = null,
) : SurveyResponse(id, createdAt, title, content, author, photoPath, endsAt, allowCount, baseAnswers, type)
