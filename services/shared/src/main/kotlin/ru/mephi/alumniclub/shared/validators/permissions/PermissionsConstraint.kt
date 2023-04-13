package ru.mephi.alumniclub.shared.validators.permissions

import javax.validation.Constraint
import javax.validation.Payload
import kotlin.reflect.KClass

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [PermissionsValidator::class])
annotation class PermissionsConstraint(
    val message: String = "validation.permission.scope",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)
