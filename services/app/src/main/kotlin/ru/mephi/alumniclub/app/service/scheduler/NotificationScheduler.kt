package ru.mephi.alumniclub.app.service.scheduler

import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import ru.mephi.alumniclub.app.database.repository.notification.NotificationDao
import ru.mephi.alumniclub.shared.logging.AlumniLogger
import java.time.LocalDateTime

@Service
class NotificationScheduler(
    private val dao: NotificationDao, private val logger: AlumniLogger
) {
    @Scheduled(fixedDelay = 24 * 3600 * 1000L) // 1 day
    fun handleNotifications() {
        logger.info("Start deleting old notifications")
        dao.deleteAllByCreatedAtBefore(LocalDateTime.now().minusDays(30))
        logger.info("Deleting old notifications has been completed")
    }
}