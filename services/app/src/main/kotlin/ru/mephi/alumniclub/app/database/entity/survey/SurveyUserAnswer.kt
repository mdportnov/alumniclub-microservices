package ru.mephi.alumniclub.app.database.entity.survey

import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import ru.mephi.alumniclub.app.database.entity.user.User
import ru.mephi.alumniclub.shared.database.entity.AbstractCreatedAtEntity
import javax.persistence.*

@Entity
@Table(name = "SurveyUserAnswer")
class SurveyUserAnswer(
    @OnDelete(action = OnDeleteAction.CASCADE)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "surveyId")
    var survey: Survey,

    @OnDelete(action = OnDeleteAction.CASCADE)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    var answered: User,

    @OneToMany(cascade = [CascadeType.ALL], fetch = FetchType.LAZY, mappedBy = "answer")
    var surveySelectedAnswers: MutableList<SurveySelectedAnswers> = mutableListOf(),

    @OneToOne(mappedBy = "surveyUserAnswer", optional = true, cascade = [CascadeType.ALL])
    @PrimaryKeyJoinColumn
    var freeAnswer: SurveyFreeAnswer? = null
) : AbstractCreatedAtEntity() {

    val selectedAnswers: MutableList<SurveyItemAnswer>
        get() = surveySelectedAnswers.map { it.itemAnswer }.toMutableList()
}
