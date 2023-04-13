package ru.mephi.alumniclub.app.database.repository.outbox

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import ru.mephi.alumniclub.app.database.entity.outbox.OutboxMessage
import ru.mephi.alumniclub.shared.database.repository.AbstractRepository
import java.util.UUID

@Repository
interface OutboxMessageDao : AbstractRepository<OutboxMessage>