package ru.mephi.alumniclub.app.database.repository.feed

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.PagingAndSortingRepository
import ru.mephi.alumniclub.app.database.entity.feed.PublicationFeed
import ru.mephi.alumniclub.shared.database.repository.AbstractRepository
import java.util.*

interface PublicationFeedDao : AbstractRepository<PublicationFeed>, PagingAndSortingRepository<PublicationFeed, Long> {
    fun existsByName(name: String?): Boolean
    fun existsByNameAndIdNot(name: String?, id: Long): Boolean
    fun findByName(name: String): Optional<PublicationFeed>
    fun findByNameStartsWith(query: String, pageable: Pageable): Page<PublicationFeed>
}
