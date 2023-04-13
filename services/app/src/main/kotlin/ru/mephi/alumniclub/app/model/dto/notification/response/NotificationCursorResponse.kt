package ru.mephi.alumniclub.app.model.dto.notification.response

import ru.mephi.alumniclub.app.model.dto.CursorResponse
import ru.mephi.alumniclub.shared.dto.AbstractCreatedAtResponse

class NotificationCursorResponse<T : AbstractCreatedAtResponse<*>>(
    content: List<T>,
    numberOfElements: Long,
    var info: UserNotificationInfoResponse
) : CursorResponse<T>(content, numberOfElements)