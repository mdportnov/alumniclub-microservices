package ru.mephi.alumniclub.app.database.repository.user

import ru.mephi.alumniclub.app.database.entity.user.Biography
import ru.mephi.alumniclub.shared.database.repository.CommonRepositoryWithUser

interface BioDao : CommonRepositoryWithUser<Biography> {
    fun findByUserId(id: Long): Biography
    fun deleteBiographyByUserId(id: Long): Long
}
