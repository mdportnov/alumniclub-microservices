package ru.mephi.alumniclub.imageservice.database.entity.publication

import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(name = "Publication")
class Publication(
    title: String,
    content: String,
    photoPath: String? = null,
) : AbstractPublication(
    title, content, photoPath
)