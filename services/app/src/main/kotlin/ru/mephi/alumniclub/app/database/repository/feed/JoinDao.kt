package ru.mephi.alumniclub.app.database.repository.feed

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.PagingAndSortingRepository
import ru.mephi.alumniclub.app.database.entity.publication.Join
import ru.mephi.alumniclub.app.database.entity.user.User
import ru.mephi.alumniclub.shared.database.repository.AbstractRepository
import java.util.*

interface JoinDao : AbstractRepository<Join>, PagingAndSortingRepository<Join, Long> {
    fun existsByEventIdAndUserId(eventId: UUID, userId: Long): Boolean
    fun deleteByEventIdAndUser(eventId: UUID, user: User)
    fun findByEventId(eventId: UUID, pageable: Pageable): Page<Join>
    fun findByEventId(eventId: UUID): List<Join>
    fun countByEventId(eventId: UUID): Long
}
