package ru.mephi.alumniclub.app.service.impl

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.springframework.amqp.core.AmqpTemplate
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import ru.mephi.alumniclub.app.database.entity.outbox.OutboxMessage
import ru.mephi.alumniclub.app.database.entity.outbox.OutboxMessageStatus
import ru.mephi.alumniclub.app.service.OutboxService
import ru.mephi.alumniclub.app.service.RabbitService
import ru.mephi.alumniclub.shared.dto.mail.AbstractMail
import ru.mephi.alumniclub.shared.logging.AlumniLogger
import ru.mephi.alumniclub.shared.util.enums.RabbitMessageType

@Service
class RabbitServiceImpl(
    private val rabbitTemplate: AmqpTemplate,
    private val jackson: ObjectMapper,
    @Lazy
    private val outboxService: OutboxService,
    private val logger: AlumniLogger
) : RabbitService {

    /**
     * Sends message to RabbitMQ queue. Uses the outbox pattern in case RabbitMQ is not available.
     * Does it in new transaction. This function not throws exceptions.
     *
     * @param message the object to be sent to the queue
     * @param type specifies which queue to send the object to
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    override fun sendMessage(message: Any, type: RabbitMessageType) {
        val payload = jackson.writeValueAsString(message)
        val outboxMessage = outboxService.save(OutboxMessage(payload, type))
        try {
            outboxService.handleMessage(outboxMessage)
        } catch (e: Exception) {
            outboxService.changeMessageStatus(outboxMessage, OutboxMessageStatus.ERRORED)
            logger.error("Rabbit message send error in queue [${type.path}]", e)
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    override fun sendMessage(message: AbstractMail) {
        sendMessage(message, message.type)
    }

    override fun sendToRabbit(message: OutboxMessage) {
        return sendToRabbit(message.type, message.payload)
    }


    private fun sendToRabbit(type: RabbitMessageType, message: String) {
        val payload = jackson.readValue<Any>(message)
        rabbitTemplate.convertAndSend(type.path, payload) { msg ->
            msg.messageProperties.headers.remove("__TypeId__")
            msg
        }
    }
}