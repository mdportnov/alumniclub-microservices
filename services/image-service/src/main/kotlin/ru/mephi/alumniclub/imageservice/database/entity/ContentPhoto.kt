package ru.mephi.alumniclub.imageservice.database.entity

import ru.mephi.alumniclub.imageservice.database.entity.publication.AbstractPublication
import ru.mephi.alumniclub.shared.database.entity.AbstractCreatedAtEntity
import javax.persistence.*

@Entity
@Table(name = "ContentPhoto", indexes = [Index(columnList = "photoPath")])
class ContentPhoto(
    @Column
    var photoPath: String,
) : AbstractCreatedAtEntity() {
    constructor() : this("")

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "contentId", nullable = true)
    var content: AbstractPublication? = null
}