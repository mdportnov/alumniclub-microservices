package ru.mephi.alumniclub.app.database.entity.form

import org.hibernate.annotations.Formula
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import ru.mephi.alumniclub.app.database.entity.atom.Merch
import javax.persistence.*

@Entity
class FormBuyMerch(
    @OnDelete(action = OnDeleteAction.CASCADE)
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "merchId", nullable = false)
    var merch: Merch
) : AbstractFormEntity() {
    @FormField
    @Formula("(select m.name from Merch m where m.id = merchId)")
    var merchName: String? = null
}
