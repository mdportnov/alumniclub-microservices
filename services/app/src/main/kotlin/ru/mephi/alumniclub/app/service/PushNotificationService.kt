package ru.mephi.alumniclub.app.service

import ru.mephi.alumniclub.app.database.entity.fcm.PushNotification

interface PushNotificationService {
    fun save(entity: PushNotification): PushNotification
    fun deleteById(id: Long)
    fun delete(entity: PushNotification)
    fun findAll(): Iterable<PushNotification>
}