package ru.mephi.alumniclub.app.database.repository.community

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.PagingAndSortingRepository
import ru.mephi.alumniclub.app.database.entity.community.Community
import ru.mephi.alumniclub.app.database.entity.user.User
import ru.mephi.alumniclub.shared.database.repository.AbstractRepository
import java.time.LocalDateTime
import java.util.*

interface CommunityDao : AbstractRepository<Community>, PagingAndSortingRepository<Community, Long> {
    fun findByNameStartsWith(prefix: String, pageable: Pageable): Page<Community>
    fun existsByName(name: String): Boolean
    fun existsByNameAndIdNot(name: String, id: Long): Boolean
    fun findByName(name: String): Optional<Community>

    fun findByUsersNotContainingAndHiddenFalseAndCreatedAtBefore(
        user: User, before: LocalDateTime, pageable: Pageable
    ): Page<Community>

    fun findByUsersNotContainingAndHiddenFalseAndCreatedAtAfter(
        user: User, after: LocalDateTime, pageable: Pageable
    ): Page<Community>
}
