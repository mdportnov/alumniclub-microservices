package ru.mephi.alumniclub.app.controller.validator.event

import ru.mephi.alumniclub.shared.validators.AbstractValidator
import ru.mephi.alumniclub.app.model.dto.feed.request.EventUpdateRequest

class EventTimeValidator : AbstractValidator<EventTimeConstraint, EventUpdateRequest>() {
    override fun initialize(annotation: EventTimeConstraint) {
        message = i18n(annotation.message)
    }

    override fun checkViolations(value: EventUpdateRequest) {
        if (value.time <= value.publicationDate)
            addViolation()
    }
}
