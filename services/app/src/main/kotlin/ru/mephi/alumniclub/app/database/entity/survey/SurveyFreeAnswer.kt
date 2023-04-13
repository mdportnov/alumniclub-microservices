package ru.mephi.alumniclub.app.database.entity.survey

import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import ru.mephi.alumniclub.shared.database.entity.AbstractEntity
import ru.mephi.alumniclub.shared.util.constants.extraLargeLength
import javax.persistence.*

@Entity
@Table(name = "SurveyFreeAnswer")
class SurveyFreeAnswer(
    @OnDelete(action = OnDeleteAction.CASCADE)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "surveyAnswerId", nullable = false)
    @MapsId
    var surveyUserAnswer: SurveyUserAnswer,

    @Column(name = "freeAnswer", nullable = false, length = extraLargeLength)
    var freeAnswer: String
) : AbstractEntity()
