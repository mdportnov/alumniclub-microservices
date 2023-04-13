package ru.mephi.alumniclub.app.controller.validator.notification

import javax.validation.Constraint
import javax.validation.Payload
import kotlin.reflect.KClass

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [NotificationTypeValidator::class])
annotation class NotificationTypeConstraint(
    val message: String = "validation.notification.receiversIds",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)