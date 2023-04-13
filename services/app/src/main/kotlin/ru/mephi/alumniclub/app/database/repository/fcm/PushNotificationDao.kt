package ru.mephi.alumniclub.app.database.repository.fcm

import org.springframework.stereotype.Repository
import ru.mephi.alumniclub.app.database.entity.fcm.PushNotification
import ru.mephi.alumniclub.shared.database.repository.AbstractRepository

@Repository
interface PushNotificationDao : AbstractRepository<PushNotification>