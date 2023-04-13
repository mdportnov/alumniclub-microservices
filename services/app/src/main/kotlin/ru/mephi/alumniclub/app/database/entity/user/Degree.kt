package ru.mephi.alumniclub.app.database.entity.user

import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import ru.mephi.alumniclub.app.model.enumeration.user.DegreeType
import ru.mephi.alumniclub.shared.database.entity.AbstractCreatedAtEntity
import ru.mephi.alumniclub.shared.util.constants.extraLargeLength
import ru.mephi.alumniclub.shared.util.constants.userDataLength
import javax.persistence.*

@Entity
@Table(name = "Degree")
class Degree(

    @Column(nullable = false)
    var enrollment: Int,

    @Column
    var graduation: Int?,

    @Enumerated(EnumType.STRING)
    @Column(
        nullable = false,
        columnDefinition = "enum( 'LYCEUM', 'BACHELOR', 'MASTER', 'SPECIALTY', 'POSTGRADUATE', 'WORKER' )"
    )
    var degreeType: DegreeType,

    @Column(nullable = false, length = userDataLength)
    var description: String

) : AbstractCreatedAtEntity() {
    @OnDelete(action = OnDeleteAction.CASCADE)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "userId")
    lateinit var user: User
}
