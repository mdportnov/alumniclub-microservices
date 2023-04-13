package ru.mephi.alumniclub.app.database.repository.survey

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ru.mephi.alumniclub.app.database.entity.survey.SurveyFreeAnswer
import ru.mephi.alumniclub.shared.database.repository.AbstractRepository

@Repository
interface SurveyFreeAnswerDao : AbstractRepository<SurveyFreeAnswer>, JpaRepository<SurveyFreeAnswer, Long> {
}
