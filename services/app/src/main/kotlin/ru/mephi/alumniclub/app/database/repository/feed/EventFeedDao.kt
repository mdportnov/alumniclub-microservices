package ru.mephi.alumniclub.app.database.repository.feed

import org.springframework.data.repository.PagingAndSortingRepository
import ru.mephi.alumniclub.app.database.entity.feed.EventFeed
import ru.mephi.alumniclub.shared.database.repository.AbstractRepository

interface EventFeedDao : AbstractRepository<EventFeed>, PagingAndSortingRepository<EventFeed, Long> {
    fun existsByName(name: String): Boolean
    fun existsByNameAndIdNot(name: String, id: Long): Boolean
}
