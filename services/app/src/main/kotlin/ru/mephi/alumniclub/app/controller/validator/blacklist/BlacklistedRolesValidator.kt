package ru.mephi.alumniclub.app.controller.validator.blacklist

import ru.mephi.alumniclub.app.model.enumeration.user.Role
import ru.mephi.alumniclub.shared.validators.AbstractValidator

class BlacklistedRolesValidator : AbstractValidator<BlacklistedRolesConstraint, Set<Role>>() {
    private lateinit var blacklist: Set<Role>

    override fun initialize(annotation: BlacklistedRolesConstraint) {
        blacklist = setOf(*annotation.values)
        message = i18n(annotation.message)
    }

    override fun checkViolations(value: Set<Role>) {
        val intersection = blacklist.intersect(value)
        if (intersection.isNotEmpty())
            addViolation("values" to intersection.joinToString(", "))
    }
}
