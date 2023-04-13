package ru.mephi.alumniclub.app.database.repository.form

import ru.mephi.alumniclub.app.database.entity.form.FormFindMentor
import java.util.*

interface FormFindMentorDao : AbstractFormDao<FormFindMentor> {
    fun countAllByAuthorIdAndMentorId(authorId: Long, mentorId: Long): Long
}
