package ru.mephi.alumniclub.app.database.entity.notification

import ru.mephi.alumniclub.app.database.entity.feed.AbstractFeed
import ru.mephi.alumniclub.app.model.enumeration.NotificationCategory
import java.util.*

interface NotificationHolder {
    val id: UUID
    val category: NotificationCategory
    val title: String
    val feed: AbstractFeed?
}