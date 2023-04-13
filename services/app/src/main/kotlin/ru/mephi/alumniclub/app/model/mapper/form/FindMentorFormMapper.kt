package ru.mephi.alumniclub.app.model.mapper.form;

import org.springframework.stereotype.Component
import ru.mephi.alumniclub.app.database.entity.form.FormFindMentor
import ru.mephi.alumniclub.app.database.entity.user.User
import ru.mephi.alumniclub.app.model.dto.form.request.AbstractFormRequest.FormFindMentorRequest
import ru.mephi.alumniclub.app.model.enumeration.FormType

@Component
class FindMentorFormMapper : AbstractFormMapper<FormFindMentor>() {
    override val type: FormType = FormType.FindMentor

    fun asEntity(author: User, mentor: User, request: FormFindMentorRequest): FormFindMentor {
        return FormFindMentor(
            motivation = request.motivation,
            description = request.description,
            targets = request.targets,
            mentor = mentor,
            mentorId = mentor.id,
        ).apply { this.author = author }
    }
}
