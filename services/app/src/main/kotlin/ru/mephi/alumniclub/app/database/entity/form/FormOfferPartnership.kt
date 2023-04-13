package ru.mephi.alumniclub.app.database.entity.form

import ru.mephi.alumniclub.shared.util.constants.extraLargeLength
import ru.mephi.alumniclub.shared.util.constants.largeLength
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Entity

@Entity
class FormOfferPartnership(
    @FormField
    @Column(nullable = false, length = largeLength)
    var requirements: String,

    @FormField
    @Column(length = extraLargeLength)
    var selfDescription: String,

    @FormField
    @Column(nullable = false, length = largeLength)
    var name: String,

    @FormField
    @Column(nullable = false)
    var currentUntil: LocalDateTime,

    @FormField
    @Column(nullable = false, length = extraLargeLength)
    var projectDescription: String,

    @FormField
    @Column(length = extraLargeLength)
    var helpDescription: String,
) : AbstractFormEntity()
