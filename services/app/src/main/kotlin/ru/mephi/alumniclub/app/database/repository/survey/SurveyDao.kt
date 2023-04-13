package ru.mephi.alumniclub.app.database.repository.survey

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import ru.mephi.alumniclub.app.database.entity.survey.Survey
import java.time.LocalDateTime
import java.util.*

@Repository
interface SurveyDao : CrudRepository<Survey, UUID>, JpaRepository<Survey, UUID> {
    fun findByCreatedAtAfterAndEndsAtAfter(
        createdAt: LocalDateTime,
        endsAt: LocalDateTime,
        page: Pageable
    ): Page<Survey>

    fun findByCreatedAtBeforeAndEndsAtAfter(
        createdAt: LocalDateTime,
        endsAt: LocalDateTime,
        page: Pageable
    ): Page<Survey>

    @Modifying
    @Query("DELETE FROM Survey s WHERE s.id = :surveyId")
    fun delete(@Param("surveyId") surveyId: Long)

}
