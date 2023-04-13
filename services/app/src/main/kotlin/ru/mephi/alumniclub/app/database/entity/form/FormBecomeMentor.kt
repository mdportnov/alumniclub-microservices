package ru.mephi.alumniclub.app.database.entity.form

import ru.mephi.alumniclub.shared.util.constants.extraLargeLength
import ru.mephi.alumniclub.shared.util.constants.largeLength
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(name = "FormBecomeMentor")
class FormBecomeMentor(

    @FormField
    @Column(name = "company", nullable = true, length = largeLength)
    var company: String?,

    @FormField
    @Column(name = "position", nullable = true, length = largeLength)
    var position: String?,

    @FormField
    @Column(name = "expertArea", nullable = true, length = largeLength)
    var expertArea: String?,

    @FormField
    @Column(name = "whyAreYouMentor", nullable = true, length = extraLargeLength)
    var whyAreYouMentor: String?,

    @FormField
    @Column(name = "helpArea", nullable = true, length = extraLargeLength)
    var helpArea: String?,

    @FormField
    @Column(name = "timeForMentoring", nullable = true, length = extraLargeLength)
    var timeForMentoring: String?,

    @FormField
    @Column(name = "formatsOfInteraction", nullable = true, length = extraLargeLength)
    var formatsOfInteraction: String?,
) : AbstractFormEntity()
