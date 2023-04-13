package ru.mephi.alumniclub.app.database.entity.form

import ru.mephi.alumniclub.shared.util.constants.extraLargeLength
import javax.persistence.Column
import javax.persistence.Entity

@Entity
class FormOfferCommunity(
    @FormField
    @Column(name = "text", nullable = false, length = extraLargeLength)
    var text: String,
) : AbstractFormEntity()
