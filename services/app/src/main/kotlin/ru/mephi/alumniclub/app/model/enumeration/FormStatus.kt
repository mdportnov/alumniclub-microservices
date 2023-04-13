package ru.mephi.alumniclub.app.model.enumeration

enum class FormStatus(val text: String) {
    UNSEEN("Не просмотрено"), SEEN("Просмотрено"),
    ACCEPTED("Обработано"), DECLINED("Отклонено")
}