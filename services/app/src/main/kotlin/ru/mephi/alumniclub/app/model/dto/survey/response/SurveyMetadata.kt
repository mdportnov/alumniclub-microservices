package ru.mephi.alumniclub.app.model.dto.survey.response

class SurveyMetadata(
    val info: SurveyAnswersInfoResponse,
    val answer: SurveyUserAnswerResponse? = null,
)