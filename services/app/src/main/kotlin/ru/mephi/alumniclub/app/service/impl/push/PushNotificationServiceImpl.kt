package ru.mephi.alumniclub.app.service.impl.push

import org.springframework.stereotype.Service
import ru.mephi.alumniclub.app.database.entity.fcm.PushNotification
import ru.mephi.alumniclub.app.database.repository.fcm.PushNotificationDao
import ru.mephi.alumniclub.app.service.PushNotificationService

@Service
class PushNotificationServiceImpl(
    private val dao: PushNotificationDao
) : PushNotificationService {
    override fun save(entity: PushNotification): PushNotification {
        return dao.save(entity)
    }

    override fun deleteById(id: Long) {
        return dao.deleteById(id)
    }

    override fun delete(entity: PushNotification) {
        return dao.delete(entity)
    }

    override fun findAll(): Iterable<PushNotification> {
        return dao.findAll()
    }
}