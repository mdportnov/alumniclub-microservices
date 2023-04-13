package ru.mephi.alumniclub.app.service

import ru.mephi.alumniclub.app.database.entity.outbox.OutboxMessage
import ru.mephi.alumniclub.shared.dto.mail.AbstractMail
import ru.mephi.alumniclub.shared.util.enums.RabbitMessageType

interface RabbitService {
    fun sendMessage(message: Any, type: RabbitMessageType)
    fun sendMessage(message: AbstractMail)
    fun sendToRabbit(message: OutboxMessage)
}