package ru.mephi.alumniclub.app.model.dto.excel.getters

import ru.mephi.alumniclub.app.model.dto.excel.ExcelFieldGetter
import ru.mephi.alumniclub.app.model.dto.user.UserExportDTO
import ru.mephi.alumniclub.app.model.enumeration.user.DegreeType

fun userFieldGetters() = listOf<ExcelFieldGetter<UserExportDTO>>(
    ExcelFieldGetter("id") { it.user.id },
    ExcelFieldGetter("ФИО") { it.user.fullName },
    ExcelFieldGetter("Почта") { it.user.email },
    ExcelFieldGetter("Возраст") { it.user.age },
    ExcelFieldGetter("Дата рождения") { it.user.birthday },
    ExcelFieldGetter("Дата регистрации") { it.user.createdAt },
    ExcelFieldGetter("Админ") { it.user.admin },
    ExcelFieldGetter("Забанен") { it.user.banned },
    ExcelFieldGetter("Пол") { if (it.user.gender) "МУЖ" else "ЖЕН" },
    ExcelFieldGetter("Телефон") { it.user.phone },
    ExcelFieldGetter("Vk") { it.user.vk },
    ExcelFieldGetter("Tg") { it.user.tg },
    ExcelFieldGetter("Лицеист") { it.user.lyceum },
    ExcelFieldGetter("Студент") { it.user.student },
    ExcelFieldGetter("Выпускник") { it.user.alumnus },
    ExcelFieldGetter("Ментор") { it.user.mentor },
    ExcelFieldGetter("Работник") { it.user.worker },
    ExcelFieldGetter("Фото") { it.user.photoPath },

    ExcelFieldGetter(" ") { " " },

    ExcelFieldGetter("Страна") { it.bio.country },
    ExcelFieldGetter("Город") { it.bio.city },
    ExcelFieldGetter("Сфера деятельности") { it.bio.jobArea },
    ExcelFieldGetter("Компания") { it.bio.company },
    ExcelFieldGetter("Должность") { it.bio.job },
    ExcelFieldGetter("Опыт работы") { it.bio.experience },
    ExcelFieldGetter("Хобби") { it.bio.hobbies },

    ExcelFieldGetter(" ") { " " },

    ExcelFieldGetter("Лицеист") { it.getDegree(DegreeType.LYCEUM)?.description },
    ExcelFieldGetter("Начало") { it.getDegree(DegreeType.LYCEUM)?.enrollment },
    ExcelFieldGetter("Окончание") { it.getDegree(DegreeType.LYCEUM)?.graduation },

    ExcelFieldGetter("Бакалавр") { it.getDegree(DegreeType.BACHELOR)?.description },
    ExcelFieldGetter("Начало") { it.getDegree(DegreeType.BACHELOR)?.enrollment },
    ExcelFieldGetter("Окончание") { it.getDegree(DegreeType.BACHELOR)?.graduation },

    ExcelFieldGetter("Магистратура") { it.getDegree(DegreeType.MASTER)?.description },
    ExcelFieldGetter("Начало") { it.getDegree(DegreeType.MASTER)?.enrollment },
    ExcelFieldGetter("Окончание") { it.getDegree(DegreeType.MASTER)?.graduation },

    ExcelFieldGetter("Специалитет") { it.getDegree(DegreeType.SPECIALTY)?.description },
    ExcelFieldGetter("Начало") { it.getDegree(DegreeType.SPECIALTY)?.enrollment },
    ExcelFieldGetter("Окончание") { it.getDegree(DegreeType.SPECIALTY)?.graduation },

    ExcelFieldGetter("Аспирантура") { it.getDegree(DegreeType.POSTGRADUATE)?.description },
    ExcelFieldGetter("Начало") { it.getDegree(DegreeType.POSTGRADUATE)?.enrollment },
    ExcelFieldGetter("Окончание") { it.getDegree(DegreeType.POSTGRADUATE)?.graduation },

    ExcelFieldGetter("Работа") { it.getDegree(DegreeType.WORKER)?.description },
    ExcelFieldGetter("Начало") { it.getDegree(DegreeType.WORKER)?.enrollment },
    ExcelFieldGetter("Окончание") { it.getDegree(DegreeType.WORKER)?.graduation },

    ExcelFieldGetter(" ") { " " },

    ExcelFieldGetter("Доступен") { it.mentor?.available },
    ExcelFieldGetter("Компания") { it.mentor?.company },
    ExcelFieldGetter("Экспертная область") { it.mentor?.expertArea },
    ExcelFieldGetter("Форматы взаимодействия") { it.mentor?.formatsOfInteractions },
    ExcelFieldGetter("Ссылка на собеседование") { it.mentor?.interviewLink },
    ExcelFieldGetter("Позиция") { it.mentor?.position },
    ExcelFieldGetter("Теги") { it.mentor?.tags },
    ExcelFieldGetter("Почему ты ментор") { it.mentor?.whyAreYouMentor },
)
