package ru.mephi.alumniclub.app.controller.validator.survey

import javax.validation.Constraint
import javax.validation.Payload
import kotlin.reflect.KClass

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [SurveyAllowCountValidator::class])
annotation class SurveyAllowCountConstraint(
    val message: String = "validation.survey.choiceAllowCount",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)