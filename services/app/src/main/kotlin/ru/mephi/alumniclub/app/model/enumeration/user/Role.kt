package ru.mephi.alumniclub.app.model.enumeration.user

enum class Role(val community: String) {
    LYCEUM("Лицеисты"),
    STUDENT("Студенты"),
    ALUMNUS("Выпускники"),
    WORKER("Сотрудники"),
    MENTOR("Наставники"),
    MODERATOR("Модераторы"),
    ADMIN("Администраторы")
}
