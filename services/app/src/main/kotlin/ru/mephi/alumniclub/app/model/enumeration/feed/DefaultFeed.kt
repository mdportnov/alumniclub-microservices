package ru.mephi.alumniclub.app.model.enumeration.feed

import ru.mephi.alumniclub.app.model.enumeration.NotificationCategory
import ru.mephi.alumniclub.app.model.exception.common.ResourceNotFoundException

enum class DefaultFeed(val category: NotificationCategory, var id: Long = 0) {
    partnerships(NotificationCategory.PARTNERSHIPS),
    achievements(NotificationCategory.ACHIEVEMENTS);

    companion object {
        fun fromId(id: Long) = values().associateBy { it.id }[id] ?: throw ResourceNotFoundException()
    }
}
