package ru.mephi.alumniclub.app.database.repository.feed

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Lock
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.data.repository.query.Param
import ru.mephi.alumniclub.app.database.entity.publication.Publication
import java.time.LocalDateTime
import java.util.*
import javax.persistence.LockModeType

interface PublicationDao : CrudRepository<Publication, UUID>, PagingAndSortingRepository<Publication, UUID> {
    fun existsByHumanUrl(url: String): Boolean

    fun existsByHumanUrlAndIdNot(url: String, id: UUID): Boolean

    fun findByHumanUrlAndPublicationDateIsBefore(
        url: String,
        date: LocalDateTime = LocalDateTime.now()
    ): Optional<Publication>

    fun findByTitleStartsWithAndPublicationDateIsBeforeAndHiddenFalse(
        prefix: String,
        pageable: Pageable,
        date: LocalDateTime = LocalDateTime.now(),
    ): Page<Publication>

    fun findByFeedIdAndTitleStartsWith(feedId: Long, prefix: String, pageable: Pageable): Page<Publication>

    fun findByFeedIdAndTitleStartsWithAndHiddenFalse(
        feedId: Long,
        prefix: String,
        pageable: Pageable,
    ): Page<Publication>

    fun findByFeedIdAndCreatedAtBeforeAndPublicationDateIsBeforeAndHiddenFalse(
        feedId: Long,
        createdAt: LocalDateTime,
        pageable: Pageable,
        date: LocalDateTime = LocalDateTime.now(),
    ): Page<Publication>

    fun findByFeedIdAndCreatedAtAfterAndPublicationDateIsBeforeAndHiddenFalse(
        feedId: Long,
        createdAt: LocalDateTime,
        pageable: Pageable,
        date: LocalDateTime = LocalDateTime.now(),
    ): Page<Publication>

    fun findByFeedIdAndPublicationDateIsBeforeAndHiddenFalse(
        feedId: Long,
        pageable: Pageable,
        date: LocalDateTime = LocalDateTime.now(),
    ): Page<Publication>

    @Modifying
    @Query("DELETE FROM Publication p WHERE p.id = :publicationId")
    fun delete(@Param("publicationId") publicationId: Long)

    @Modifying
    @Query("UPDATE Publication p SET p.hidden = :hidden WHERE p.feed.id = :feedId")
    fun setHiddenToAllPublicationsInFeed(@Param("feedId") feedId: Long, @Param("hidden") hidden: Boolean)
}
