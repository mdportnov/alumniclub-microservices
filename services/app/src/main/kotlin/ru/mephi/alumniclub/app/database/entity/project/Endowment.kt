package ru.mephi.alumniclub.app.database.entity.project

import javax.persistence.DiscriminatorValue
import javax.persistence.Entity

@Entity
@DiscriminatorValue(Endowment.DISCRIMINATOR_VALUE)
class Endowment(
    name: String,
    description: String,
    archive: Boolean = false,
    photoPath: String? = null,
    color: String? = null
) : AbstractProject(
    name = name,
    description = description,
    archive = archive,
    photoPath = photoPath,
    color = color
) {
    companion object {
        const val DISCRIMINATOR_VALUE = "ENDOWMENT"
    }
}
