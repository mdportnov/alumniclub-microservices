package ru.mephi.alumniclub.app.model.dto.user

import ru.mephi.alumniclub.app.database.entity.user.Biography
import ru.mephi.alumniclub.app.database.entity.user.MentorData
import ru.mephi.alumniclub.app.database.entity.user.User
import ru.mephi.alumniclub.app.model.enumeration.user.DegreeType

class UserExportDTO(
    val user: User,
    val bio: Biography,
    val degrees: List<DegreeDTO>,
    val mentor: MentorData?
) {
    fun getDegree(type: DegreeType) = degrees.find { it.degree == type }
}