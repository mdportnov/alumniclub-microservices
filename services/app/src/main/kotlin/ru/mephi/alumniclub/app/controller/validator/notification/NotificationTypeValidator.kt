package ru.mephi.alumniclub.app.controller.validator.notification

import ru.mephi.alumniclub.shared.validators.AbstractValidator
import ru.mephi.alumniclub.app.model.dto.notification.request.AbstractNotificationRequest
import ru.mephi.alumniclub.app.model.enumeration.fcm.BroadcastType

class NotificationTypeValidator : AbstractValidator<NotificationTypeConstraint, AbstractNotificationRequest>() {
    override fun initialize(annotation: NotificationTypeConstraint) {
        message = i18n(annotation.message)
    }

    override fun checkViolations(value: AbstractNotificationRequest) {
        if (value.broadcastType != BroadcastType.ALL && value.receiversIds.isEmpty())
            addViolation()
    }
}
