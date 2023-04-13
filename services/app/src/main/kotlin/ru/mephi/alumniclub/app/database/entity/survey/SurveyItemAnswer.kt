package ru.mephi.alumniclub.app.database.entity.survey

import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import ru.mephi.alumniclub.shared.database.entity.AbstractEntity
import ru.mephi.alumniclub.shared.util.constants.smallLength
import javax.persistence.*

@Entity
@Table(name = "SurveyItemAnswer")
class SurveyItemAnswer(
    @OnDelete(action = OnDeleteAction.CASCADE)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "surveyId")
    var survey: Survey? = null,

    @Column(name = "answer", nullable = false, length = smallLength)
    var answer: String
) : AbstractEntity()
