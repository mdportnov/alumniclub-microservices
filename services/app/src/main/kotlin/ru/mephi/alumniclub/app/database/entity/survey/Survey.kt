package ru.mephi.alumniclub.app.database.entity.survey

import ru.mephi.alumniclub.app.database.entity.feed.AbstractFeed
import ru.mephi.alumniclub.app.database.entity.publication.AbstractPublication
import ru.mephi.alumniclub.app.model.enumeration.NotificationCategory
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "Survey")
class Survey(
    title: String,
    content: String,

    @Column(name = "endsAt", nullable = false)
    var endsAt: LocalDateTime,

    @Column(name = "allowCount", nullable = false)
    var allowCount: Int,

    @OneToMany(mappedBy = "survey", cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.LAZY)
    var baseAnswers: MutableList<SurveyItemAnswer> = mutableListOf(),

    @Column(name = "type", nullable = false)
    var type: SurveyType
) : AbstractPublication(title, content) {

    @OneToMany(mappedBy = "survey", cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.LAZY)
    var answers: MutableList<SurveyUserAnswer> = mutableListOf()

    override val category: NotificationCategory
        get() = NotificationCategory.SURVEYS
    override val feed: AbstractFeed?
        get() = null
}
