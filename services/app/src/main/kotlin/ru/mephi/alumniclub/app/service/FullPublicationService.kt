package ru.mephi.alumniclub.app.service

import ru.mephi.alumniclub.app.database.entity.notification.NotificationHolder
import ru.mephi.alumniclub.app.database.entity.publication.AbstractPublication
import java.util.*

interface FullPublicationService {
    fun getAbstractPublication(id: UUID): AbstractPublication
    fun getAbstractPublicationOrNull(id: UUID): AbstractPublication?
    fun getPhotoUrl(id: UUID): String
    fun getMailLinkToPublication(id: UUID): String
    fun getNotificationHolder(id: UUID): NotificationHolder
    fun getNotificationHolderOrNull(id: UUID): NotificationHolder?
    fun getRedirectLinkToPublication(id: UUID): String
    fun existsNotificationHolder(publicationId: UUID): Boolean
}