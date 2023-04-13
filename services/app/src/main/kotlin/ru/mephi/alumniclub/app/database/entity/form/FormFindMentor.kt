package ru.mephi.alumniclub.app.database.entity.form

import org.hibernate.annotations.Formula
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import ru.mephi.alumniclub.app.database.entity.user.User
import ru.mephi.alumniclub.shared.util.constants.extraLargeLength
import javax.persistence.*

@Entity
class FormFindMentor(

    @FormField
    @Column(name = "motivation", nullable = true, length = extraLargeLength)
    var motivation: String?,

    @FormField
    @Column(name = "description", nullable = true, length = extraLargeLength)
    var description: String?,

    @FormField
    @Column(name = "targets", nullable = false, length = extraLargeLength)
    var targets: String?,

    @OnDelete(action = OnDeleteAction.CASCADE)
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "mentorId", nullable = false)
    var mentor: User,

    @OnDelete(action = OnDeleteAction.CASCADE)
    @Column(name = "mentorId", insertable = false, updatable = false)
    var mentorId: Long? = null,

    @FormField
    @Formula("(select concat(u.surname, ' ', u.name) from User u where u.id = mentorId)")
    var mentorName: String? = null
) : AbstractFormEntity()
