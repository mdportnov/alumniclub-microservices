package ru.mephi.alumniclub.app.model.dto.survey.response

import ru.mephi.alumniclub.app.database.entity.survey.SurveyType
import ru.mephi.alumniclub.app.model.dto.user.response.UserShortResponse
import ru.mephi.alumniclub.shared.dto.AbstractCreatedAtResponse
import ru.mephi.alumniclub.shared.dto.photo.PhotoPathed
import java.time.LocalDateTime
import java.util.*

open class SurveyResponse(
    id: UUID,
    createdAt: LocalDateTime,
    var title: String,
    var content: String,
    var author: UserShortResponse? = null,
    override var photoPath: String? = null,
    var endsAt: LocalDateTime,
    var allowCount: Int,
    var baseAnswers: MutableList<SurveyItemAnswerResponse>,
    var type: SurveyType,
) : AbstractCreatedAtResponse<UUID>(id, createdAt), PhotoPathed
