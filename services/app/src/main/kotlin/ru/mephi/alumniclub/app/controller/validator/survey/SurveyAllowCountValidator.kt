package ru.mephi.alumniclub.app.controller.validator.survey

import ru.mephi.alumniclub.app.database.entity.survey.SurveyType
import ru.mephi.alumniclub.shared.validators.AbstractValidator
import ru.mephi.alumniclub.app.model.dto.survey.request.CreateSurveyRequest

class SurveyAllowCountValidator : AbstractValidator<SurveyAllowCountConstraint, CreateSurveyRequest>() {
    override fun initialize(annotation: SurveyAllowCountConstraint) {
        message = i18n(annotation.message)
    }

    override fun checkViolations(value: CreateSurveyRequest) {
        when (value.type) {
            SurveyType.FREE_FORM -> validateFreeSurvey(value)
            SurveyType.CHOICE -> validateChoiceSurvey(value)
        }
    }

    private fun validateFreeSurvey(request: CreateSurveyRequest) {
        if (request.allowCount != 1)
            addViolation(template = "validation.survey.freeFormAllowCount")
        if (request.baseAnswers.size != 0)
            addViolation(template = "validation.survey.freeFormAnswers")
    }

    private fun validateChoiceSurvey(request: CreateSurveyRequest) {
        if (request.baseAnswers.size < 1)
            addViolation(template = "validation.survey.choiceAnswers")
        if (request.allowCount !in 1..request.baseAnswers.size)
            addViolation("baseAnswers" to request.baseAnswers.size)
    }
}
