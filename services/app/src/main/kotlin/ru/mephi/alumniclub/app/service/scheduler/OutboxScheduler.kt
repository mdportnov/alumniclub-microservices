package ru.mephi.alumniclub.app.service.scheduler

import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import ru.mephi.alumniclub.app.database.entity.outbox.OutboxMessage
import ru.mephi.alumniclub.app.database.entity.outbox.OutboxMessageStatus
import ru.mephi.alumniclub.app.service.OutboxService
import ru.mephi.alumniclub.shared.logging.AlumniLogger


@Component
class OutboxScheduler(
    private val outboxService: OutboxService,
    private val logger: AlumniLogger
) {

    /**
     * Attempts to send [OutboxMessage] to RabbitMQ that were not sent before due to an error
     * (messages with status ERRORED)
     */

    @Scheduled(fixedDelay = 30 * 60 * 1000L) // 30 min
    fun handle() {
        logger.info("Start processing emails not sent due to an error")
        outboxService.findAll().forEach { message ->
            if (message.status != OutboxMessageStatus.ERRORED) return@forEach
            try {
                outboxService.handleMessage(message)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        logger.info("Processing of emails unsent due to an error has been completed")
    }
}