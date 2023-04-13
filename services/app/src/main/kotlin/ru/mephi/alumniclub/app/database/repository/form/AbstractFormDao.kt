package ru.mephi.alumniclub.app.database.repository.form

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.NoRepositoryBean
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.transaction.annotation.Transactional
import ru.mephi.alumniclub.app.database.entity.form.AbstractFormEntity
import ru.mephi.alumniclub.app.database.entity.user.User
import ru.mephi.alumniclub.app.model.enumeration.FormStatus
import java.time.LocalDateTime
import java.util.*

@NoRepositoryBean
interface AbstractFormDao<T : AbstractFormEntity> : PagingAndSortingRepository<T, Long> {
    fun countByCreatedAtAfterAndCreatedAtBefore(after: LocalDateTime, before: LocalDateTime): Long
    fun countByCreatedAtBetweenAndAuthor(start: LocalDateTime, end: LocalDateTime, user: User): Int
    fun countAllByAuthorId(id: Long): Long
    fun findAllByAuthor(author: User): List<T>
    fun findByAuthorId(id: Long): Optional<T>
    fun findByIdAndAuthorId(id: Long, authorId: Long): Optional<T>
    fun findByOrderByCreatedAtDesc(pageable: Pageable): Page<T>
    fun findFormById(id: Long): T?
    override fun findById(id: Long): Optional<T>

    @Modifying
    @Transactional
    @Query(
        "update #{#entityName} f set f.status = :status where f.id = :id"
    )
    fun updateStatusById(id: Long, status: FormStatus): Int
}
