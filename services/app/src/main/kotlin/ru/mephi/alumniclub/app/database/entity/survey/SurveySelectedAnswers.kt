package ru.mephi.alumniclub.app.database.entity.survey

import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import ru.mephi.alumniclub.shared.database.entity.AbstractEntity
import javax.persistence.*

@Entity
@Table(name = "SurveySelectedAnswers")
class SurveySelectedAnswers(
    @OnDelete(action = OnDeleteAction.CASCADE)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "surveyAnswerId", nullable = false)
    var answer: SurveyUserAnswer,

    @OnDelete(action = OnDeleteAction.CASCADE)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "itemAnswerId", nullable = false)
    var itemAnswer: SurveyItemAnswer
) : AbstractEntity()
