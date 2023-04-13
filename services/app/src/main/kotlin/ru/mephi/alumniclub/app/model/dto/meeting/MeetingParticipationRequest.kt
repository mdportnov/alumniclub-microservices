package ru.mephi.alumniclub.app.model.dto.meeting

import ru.mephi.alumniclub.app.model.enumeration.ParticipationFormat

class MeetingParticipationRequest(
    val format: ParticipationFormat, // формат участия
    val departmentMeetup: Boolean = false, // Встречи на Кафедрах/Факультетах
    val movie: Boolean = false, // Официальный МИФИ: ректор, фильм
    val presentations: Boolean = false, // Презентации инициатив, открытых для выпускников
    val exhibition: Boolean = false, // Выставка научных достижений
    val greetings: Boolean = false, // Приветствие от современных студентов
    val enjoy: Boolean = false, // Развлекательные игровые форматы
    val performance: Boolean = false, // Хочу выступить перед сообществом выпускников
    val help: Boolean = false, // Хочу помочь
    val telegram: Boolean = false // Даю согласие на добавление в телеграм чат
)
