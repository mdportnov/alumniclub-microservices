package ru.mephi.alumniclub.app.database.repository.user

import ru.mephi.alumniclub.app.database.entity.user.UserVisibility
import ru.mephi.alumniclub.shared.database.repository.CommonRepositoryWithUser

interface UserVisibilityDao : CommonRepositoryWithUser<UserVisibility> {
    fun deleteUserVisibilityByUserId(id: Long): Long
    fun findByUserId(id: Long): UserVisibility
}
