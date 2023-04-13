package ru.mephi.alumniclub.app.model.dto.excel.getters

import ru.mephi.alumniclub.app.database.entity.publication.Join
import ru.mephi.alumniclub.app.model.dto.excel.ExcelFieldGetter

fun eventParticipationFieldGetters() = listOf<ExcelFieldGetter<Join>>(
    ExcelFieldGetter("Дата заявки") { it.createdAt },
    ExcelFieldGetter("Id") { it.user.id },
    ExcelFieldGetter("Имя") { it.user.fullName },
    ExcelFieldGetter("Возраст") { it.user.age },
    ExcelFieldGetter("Почта") { it.user.email },
    ExcelFieldGetter("Телефон") { it.user.phone },
    ExcelFieldGetter("ВКонтакте") { it.user.vk },
    ExcelFieldGetter("Телеграм") { it.user.tg },
)
