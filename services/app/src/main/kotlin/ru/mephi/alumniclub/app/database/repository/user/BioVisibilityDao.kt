package ru.mephi.alumniclub.app.database.repository.user

import ru.mephi.alumniclub.app.database.entity.user.BioVisibility
import ru.mephi.alumniclub.shared.database.repository.CommonRepositoryWithUser

interface BioVisibilityDao : CommonRepositoryWithUser<BioVisibility> {
    fun deleteBioVisibilityByUserId(id: Long): Long
    fun findByUserId(id: Long): BioVisibility
}
