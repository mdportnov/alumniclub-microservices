package ru.mephi.alumniclub.app.database.repository.feed

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.PagingAndSortingRepository
import ru.mephi.alumniclub.app.database.entity.publication.Like
import ru.mephi.alumniclub.app.database.entity.user.User
import ru.mephi.alumniclub.shared.database.repository.AbstractRepository
import java.util.*

interface LikeDao : AbstractRepository<Like>, PagingAndSortingRepository<Like, Long> {
    fun existsByPublicationIdAndUserId(publicationId: UUID, userId: Long): Boolean
    fun deleteByPublicationIdAndUser(publicationId: UUID, user: User)
    fun findByPublicationId(publicationId: UUID, pageable: Pageable): Page<Like>
    fun countByPublicationId(publicationId: UUID): Long
}
