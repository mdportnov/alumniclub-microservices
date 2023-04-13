package ru.mephi.alumniclub.app.database.entity.form

import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import ru.mephi.alumniclub.app.database.entity.partnership.Partnership
import ru.mephi.alumniclub.shared.util.constants.extraLargeLength
import javax.persistence.*

@Entity
class FormJoinPartnership(
    @FormField
    @Column(nullable = false, length = extraLargeLength)
    var contribution: String,
) : AbstractFormEntity() {
    @OnDelete(action = OnDeleteAction.CASCADE)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "partnershipId", nullable = false)
    lateinit var partnership: Partnership
}
