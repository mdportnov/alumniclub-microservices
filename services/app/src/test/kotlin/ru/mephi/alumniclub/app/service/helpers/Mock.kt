package ru.mephi.alumniclub.app.service.helpers

import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import ru.mephi.alumniclub.app.model.dto.broadcast.BroadcastOptionsDTO
import ru.mephi.alumniclub.app.model.dto.broadcast.request.AbstractBroadcastRequest
import ru.mephi.alumniclub.app.model.dto.feed.request.EventRequest
import ru.mephi.alumniclub.app.model.dto.feed.request.PublicationRequest
import ru.mephi.alumniclub.app.model.dto.notification.request.NotificationRequest
import ru.mephi.alumniclub.app.model.enumeration.NotificationCategory
import ru.mephi.alumniclub.app.model.enumeration.fcm.BroadcastType
import ru.mephi.alumniclub.shared.dto.ExtendedPageRequest
import ru.mephi.alumniclub.shared.util.Cursor
import java.time.LocalDateTime
import java.util.*

fun mockPublicationRequest() = PublicationRequest(
    title = "Publication", content = "Content",
    humanUrl = UUID.randomUUID().toString(),
    publicationDate = LocalDateTime.now().minusDays(1),
    broadcast = mockBroadcastRequest()
)

fun mockEventRequest() = EventRequest(
    title = "Event", content = "Content",
    humanUrl = UUID.randomUUID().toString(),
    broadcast = mockBroadcastRequest(),
    time = LocalDateTime.now().plusMinutes(10),
)

fun mockNotificationRequest() = NotificationRequest(
    title = "Notification", receiversIds = emptyList(),
    category = NotificationCategory.EVENTS,
    broadcastType = BroadcastType.ALL,
)

fun mockBroadcastRequest() = AbstractBroadcastRequest(
    options = BroadcastOptionsDTO(),
    receiversIds = emptyList(),
    broadcastType = BroadcastType.ALL,
)

fun mockPageRequest() = ExtendedPageRequest(1, 20, Sort.Direction.ASC, "createdAt")

fun mockCursorRequest() = Cursor(
    from = LocalDateTime.now().plusMinutes(10),
    page = PageRequest.ofSize(20),
    chronology = Cursor.Chronology.BEFORE
)
