package ru.mephi.alumniclub.app.controller.validator.blacklist

import ru.mephi.alumniclub.app.model.enumeration.user.Role
import javax.validation.Constraint
import javax.validation.Payload
import kotlin.reflect.KClass


@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [BlacklistedRolesValidator::class])
annotation class BlacklistedRolesConstraint(
    val values: Array<Role>,
    val message: String = "validation.blacklisted",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)
