package ru.mephi.alumniclub.app.database.repository.survey

import org.springframework.stereotype.Repository
import ru.mephi.alumniclub.app.database.entity.survey.SurveyItemAnswer
import ru.mephi.alumniclub.shared.database.repository.AbstractRepository

@Repository
interface SurveyItemAnswerDao : AbstractRepository<SurveyItemAnswer>
