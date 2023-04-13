package ru.mephi.alumniclub.app.controller.validator.daterange

import java.time.temporal.ChronoUnit
import javax.validation.Constraint
import javax.validation.Payload
import kotlin.reflect.KClass

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [LocalDateRangeValidator::class])
annotation class LocalDateRangeConstraint(
    val lowerBound: Long = 0,
    val upperBound: Long = 0,
    val units: ChronoUnit = ChronoUnit.DAYS,
    val message: String = "validation.range",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)