package ru.mephi.alumniclub.app.controller.validator.event

import javax.validation.Constraint
import javax.validation.Payload
import kotlin.reflect.KClass

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [EventTimeValidator::class])
annotation class EventTimeConstraint(
    val message: String = "validation.event.time",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)