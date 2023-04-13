package ru.mephi.alumniclub.app.service

import ru.mephi.alumniclub.app.database.entity.outbox.OutboxMessage
import ru.mephi.alumniclub.app.database.entity.outbox.OutboxMessageStatus

interface OutboxService {
    fun findEntityById(id: Long): OutboxMessage
    fun save(entity: OutboxMessage): OutboxMessage
    fun delete(id: Long): OutboxMessage
    fun delete(entity: OutboxMessage): OutboxMessage
    fun handleMessage(message: OutboxMessage)
    fun findAll(): Iterable<OutboxMessage>
    fun changeMessageStatus(message: OutboxMessage, errored: OutboxMessageStatus)
}