package ru.mephi.alumniclub.app.database.repository.feed

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.data.repository.query.Param
import ru.mephi.alumniclub.app.database.entity.publication.Event
import java.time.LocalDateTime
import java.util.*


interface EventDao : CrudRepository<Event, UUID>, PagingAndSortingRepository<Event, UUID> {

    fun findByTimeAfterAndPublicationDateIsBeforeAndHiddenFalse(
        time: LocalDateTime,
        pageable: Pageable,
        date: LocalDateTime = LocalDateTime.now(),
    ): List<Event>

    fun findByTitleStartsWithAndTagStartsWith(
        prefix: String, tag: String, pageable: Pageable,
    ): Page<Event>

    fun findByFeedIdAndTitleStartsWithAndTagStartsWith(
        feedId: Long, prefix: String, tag: String, pageable: Pageable,
    ): Page<Event>

    fun findByFeedIdAndTitleStartsWithAndTagStartsWithAndHiddenFalse(
        feedId: Long, prefix: String, tag: String, pageable: Pageable,
    ): Page<Event>

    fun findByCreatedAtBeforeAndPublicationDateIsBeforeAndHiddenFalse(
        createdAt: LocalDateTime,
        pageable: Pageable,
        date: LocalDateTime = LocalDateTime.now(),
    ): Page<Event>

    fun findByCreatedAtAfterAndPublicationDateIsBeforeAndHiddenFalse(
        createdAt: LocalDateTime,
        pageable: Pageable,
        date: LocalDateTime = LocalDateTime.now(),
    ): Page<Event>

    fun findByFeedIdAndCreatedAtBeforeAndPublicationDateIsBeforeAndHiddenFalse(
        feedId: Long,
        createdAt: LocalDateTime,
        pageable: Pageable,
        date: LocalDateTime = LocalDateTime.now(),
    ): Page<Event>

    fun findByFeedIdAndCreatedAtAfterAndPublicationDateIsBeforeAndHiddenFalse(
        feedId: Long,
        createdAt: LocalDateTime,
        pageable: Pageable,
        date: LocalDateTime = LocalDateTime.now(),
    ): Page<Event>

    fun existsByHumanUrlAndIdNot(humanUrl: String, id: UUID): Boolean

    fun existsByHumanUrl(humanUrl: String): Boolean

    fun findByHumanUrlAndPublicationDateIsBefore(
        humanUrl: String,
        date: LocalDateTime = LocalDateTime.now(),
    ): Optional<Event>

    @Modifying
    @Query("DELETE FROM Event e WHERE e.id = :eventId")
    fun delete(@Param("eventId") eventId: Long)

    @Modifying
    @Query("UPDATE Event e SET e.hidden = :hidden WHERE e.feed.id = :feedId")
    fun setHiddenToAllEventsInFeed(@Param("feedId") feedId: Long, @Param("hidden") hidden: Boolean)

    @Modifying
    @Query("UPDATE Event e SET e.viewsCount = e.viewsCount + 1 WHERE e.id = :eventId")
    fun incrementViewsCount(eventId: UUID?): Int
}
