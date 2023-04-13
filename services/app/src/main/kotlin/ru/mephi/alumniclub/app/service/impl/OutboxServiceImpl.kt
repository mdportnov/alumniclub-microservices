package ru.mephi.alumniclub.app.service.impl

import org.springframework.amqp.AmqpException
import org.springframework.context.annotation.Lazy
import org.springframework.data.jpa.repository.Modifying
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import ru.mephi.alumniclub.app.database.entity.outbox.OutboxMessage
import ru.mephi.alumniclub.app.database.entity.outbox.OutboxMessageStatus
import ru.mephi.alumniclub.app.database.repository.outbox.OutboxMessageDao
import ru.mephi.alumniclub.app.model.exception.common.ResourceNotFoundException
import ru.mephi.alumniclub.app.service.OutboxService
import ru.mephi.alumniclub.app.service.RabbitService

@Service
class OutboxServiceImpl(
    private val dao: OutboxMessageDao,
    @Lazy
    private val rabbitService: RabbitService
) : OutboxService {

    /**
     * Returns a [OutboxMessage] entity identified by [id].
     *
     * @param id The ID of the [OutboxMessage] entity to be retrieved.
     * @return The [OutboxMessage] entity with the specified [id].
     * @throws ResourceNotFoundException If the [OutboxMessage] entity with the specified [id] does not exist
     */
    override fun findEntityById(id: Long): OutboxMessage {
        return dao.findById(id).orElseThrow { ResourceNotFoundException(OutboxMessage::class.java) }
    }

    /**
     * Saves [OutboxMessage] to DB
     *
     * @param entity The object that will be saved
     * @return The [OutboxMessage] that was saved to DB
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    override fun save(entity: OutboxMessage): OutboxMessage {
        return dao.save(entity)
    }

    /**
     * Deletes an [OutboxMessage] by [id].
     *
     * @param id The ID of the [OutboxMessage] to delete.
     * @return [OutboxMessage] that was deleted
     */

    @Transactional
    override fun delete(id: Long): OutboxMessage {
        val entity = findEntityById(id)
        return delete(entity)
    }

    /**
     * Deletes an [OutboxMessage] by [entity] object.
     *
     * @param entity [OutboxMessage] that will be deleting.
     * @return [OutboxMessage] that was deleted
     */
    override fun delete(entity: OutboxMessage): OutboxMessage {
        dao.delete(entity)
        return entity
    }

    /**
     * Process [OutboxMessage]. Tries to send it to RabbitMQ. If everything went well, remove it from the DB.
     *
     * @throws [AmqpException] if there is a problem
     */

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    override fun handleMessage(message: OutboxMessage) {
        rabbitService.sendToRabbit(message)
        delete(message)
    }

    /**
     * Gets all [OutboxMessage] that existing in DB
     *
     * @return [Iterable] of [OutboxMessage]
     */
    override fun findAll(): Iterable<OutboxMessage> {
        return dao.findAll()
    }

    @Modifying
    @Transactional
    override fun changeMessageStatus(message: OutboxMessage, status: OutboxMessageStatus) {
        message.status = status
    }
}