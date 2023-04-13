package ru.mephi.alumniclub.app.model.dto.survey.request

import ru.mephi.alumniclub.app.controller.validator.survey.SurveyAllowCountConstraint
import ru.mephi.alumniclub.app.database.entity.survey.SurveyType
import ru.mephi.alumniclub.app.model.dto.broadcast.BroadcastOptionsDTO
import ru.mephi.alumniclub.app.model.dto.broadcast.request.AbstractBroadcastRequest
import ru.mephi.alumniclub.app.model.enumeration.fcm.BroadcastType
import java.time.LocalDateTime
import javax.validation.Valid
import javax.validation.constraints.Size

@SurveyAllowCountConstraint
class CreateSurveyRequest(
    @field:Size(max = 600)
    val title: String,
    @field:Size(max = 60000)
    val content: String,
    var endsAt: LocalDateTime,
    var allowCount: Int = 0,
    @field:Size(min = 0, max = 10)
    var baseAnswers: MutableList<SurveyItemAnswerRequest> = mutableListOf(),
    var type: SurveyType,
    @field:Valid
    var broadcast: AbstractBroadcastRequest = AbstractBroadcastRequest(
        BroadcastType.ALL,
        mutableListOf(),
        BroadcastOptionsDTO(sendEmail = false)
    )
)