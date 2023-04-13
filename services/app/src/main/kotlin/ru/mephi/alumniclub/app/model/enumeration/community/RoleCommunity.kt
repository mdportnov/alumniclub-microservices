package ru.mephi.alumniclub.app.model.enumeration.community

import ru.mephi.alumniclub.app.model.enumeration.user.Role

val roleCommunities = mutableMapOf(
    Role.LYCEUM to "Лицеисты",
    Role.STUDENT to "Студенты",
    Role.ALUMNUS to "Выпускники",
    Role.WORKER to "Сотрудники",
    Role.MENTOR to "Наставники",
    Role.ADMIN to "Администраторы",
    Role.MODERATOR to "Модераторы"
)
