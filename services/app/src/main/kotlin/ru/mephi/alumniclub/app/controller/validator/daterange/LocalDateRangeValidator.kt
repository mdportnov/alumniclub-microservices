package ru.mephi.alumniclub.app.controller.validator.daterange

import ru.mephi.alumniclub.shared.validators.AbstractValidator
import java.time.LocalDate


class LocalDateRangeValidator : AbstractValidator<LocalDateRangeConstraint, LocalDate>() {
    private lateinit var lowerBound: LocalDate
    private lateinit var upperBound: LocalDate

    override fun initialize(annotation: LocalDateRangeConstraint) {
        lowerBound = LocalDate.now().plus(annotation.lowerBound, annotation.units)
        upperBound = LocalDate.now().plus(annotation.upperBound, annotation.units)
        message = i18n(annotation.message)
    }

    override fun checkViolations(value: LocalDate) {
        if (value !in lowerBound..upperBound)
            addViolation("lowerBound" to lowerBound, "upperBound" to upperBound)
    }
}
