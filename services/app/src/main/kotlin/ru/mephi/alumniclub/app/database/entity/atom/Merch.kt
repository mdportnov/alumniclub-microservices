package ru.mephi.alumniclub.app.database.entity.atom

import ru.mephi.alumniclub.app.database.entity.form.FormBuyMerch
import ru.mephi.alumniclub.shared.database.entity.AbstractEntity
import ru.mephi.alumniclub.shared.util.constants.extraLargeLength
import ru.mephi.alumniclub.shared.util.constants.photoPathLength
import ru.mephi.alumniclub.shared.util.constants.smallLength
import javax.persistence.*

@Entity
@Table(name = "Merch")
class Merch(
    @Column(length = smallLength, name = "name", nullable = false)
    var name: String,
    @Column(length = extraLargeLength, name = "description", nullable = false)
    var description: String,
    @Column(name = "cost", nullable = false)
    var cost: Int,
    @Column(name = "availability", nullable = false)
    var availability: Boolean = true,
    @Column(length = photoPathLength, name = "photoPath")
    var photoPath: String? = null,
    @OneToMany(mappedBy = "merch", cascade = [CascadeType.ALL], orphanRemoval = true)
    var forms: MutableList<FormBuyMerch>? = mutableListOf()
) : AbstractEntity()
