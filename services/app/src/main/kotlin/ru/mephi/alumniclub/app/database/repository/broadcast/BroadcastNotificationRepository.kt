package ru.mephi.alumniclub.app.database.repository.broadcast

import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository
import ru.mephi.alumniclub.app.database.entity.broadcast.Broadcast
import java.util.*

@Repository
interface BroadcastNotificationRepository : PagingAndSortingRepository<Broadcast, UUID>
