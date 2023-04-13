package ru.mephi.alumniclub.app.model.dto.excel.getters

import ru.mephi.alumniclub.app.database.entity.publication.Join
import ru.mephi.alumniclub.app.model.dto.excel.ExcelFieldGetter

fun meetingParticipationFieldGetters() = listOf<ExcelFieldGetter<Join>>(
    ExcelFieldGetter("Формат участия") { it.meetingParticipation?.format },
    ExcelFieldGetter("Встречи на кафедрах") { it.meetingParticipation?.departmentMeetup },
    ExcelFieldGetter("Официальный МИФИ: ректор, фильм") { it.meetingParticipation?.movie },
    ExcelFieldGetter("Презентации инициатив, открытых для выпускников") { it.meetingParticipation?.presentations },
    ExcelFieldGetter("Выставка научных достижений") { it.meetingParticipation?.exhibition },
    ExcelFieldGetter("Приветствие от современных студентов") { it.meetingParticipation?.greetings },
    ExcelFieldGetter("Развлекательные игровые форматы") { it.meetingParticipation?.enjoy },
    ExcelFieldGetter("Хочу выступить перед сообществом выпускников") { it.meetingParticipation?.performance },
    ExcelFieldGetter("Хочу помочь") { it.meetingParticipation?.help },
    ExcelFieldGetter("Согласие на вступление в телеграм чат") { it.meetingParticipation?.telegram },
)
