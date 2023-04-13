package ru.mephi.alumniclub.app.util.extension

import ru.mephi.alumniclub.app.database.entity.user.User
import ru.mephi.alumniclub.app.model.enumeration.user.Role

fun Set<Role>.addProtectedRoles(user: User) = this + setOfNotNull(
    if (user.mentor) Role.MENTOR else null,
    if (user.moderator) Role.MODERATOR else null,
    if (user.admin) Role.ADMIN else null
)