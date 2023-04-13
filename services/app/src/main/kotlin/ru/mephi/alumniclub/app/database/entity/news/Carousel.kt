package ru.mephi.alumniclub.app.database.entity.news

import ru.mephi.alumniclub.shared.database.entity.AbstractCreatedAtEntity
import ru.mephi.alumniclub.shared.util.constants.largeLength
import ru.mephi.alumniclub.shared.util.constants.photoPathLength
import ru.mephi.alumniclub.shared.util.constants.smallLength
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(name = "Carousel")
class Carousel(
    @Column(name = "title", nullable = false, length = smallLength)
    var title: String,

    @Column(name = "link", length = largeLength)
    var link: String? = null,

    @Column(name = "photoPath", length = photoPathLength)
    var photoPath: String? = null
) : AbstractCreatedAtEntity()
