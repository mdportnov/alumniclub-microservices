package ru.mephi.alumniclub.app.database.repository.survey

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import ru.mephi.alumniclub.app.database.entity.survey.Survey
import ru.mephi.alumniclub.app.database.entity.survey.SurveyUserAnswer
import ru.mephi.alumniclub.app.database.entity.user.User
import ru.mephi.alumniclub.shared.database.repository.AbstractRepository
import java.util.*

@Repository
interface SurveyUserAnswerDao : AbstractRepository<SurveyUserAnswer>, JpaRepository<SurveyUserAnswer, Long> {
    fun existsByAnsweredAndSurvey(answered: User, survey: Survey): Boolean

    @Query(" SELECT count(*) FROM SurveySelectedAnswers ssa WHERE ssa.itemAnswerId = :itemAnswerId", nativeQuery = true)
    fun getCountVotesOfItemAnswer(@Param("itemAnswerId") itemAnswerId: Long): Long

    fun countAllBySurvey(survey: Survey): Long

    fun findByAnsweredAndSurvey(answered: User, survey: Survey): Optional<SurveyUserAnswer>

    @Query(
        """
        SELECT sua.answered FROM SurveyUserAnswer sua
        INNER JOIN sua.surveySelectedAnswers ssa
        WHERE sua.survey.id = :surveyId AND :itemAnswerId = ssa.itemAnswer.id
    """
    )
    fun findUsersBySurveyAndItemAnswerId(
        @Param("surveyId") surveyId: UUID,
        @Param("itemAnswerId") itemAnswerId: Long,
        page: Pageable
    ): Page<User>

    fun findAllBySurvey(survey: Survey, pageable: Pageable): Page<SurveyUserAnswer>
}
