package ru.mephi.alumniclub.app.model.mapper.form;

import org.springframework.stereotype.Component
import ru.mephi.alumniclub.app.database.entity.form.FormBecomeMentor
import ru.mephi.alumniclub.app.database.entity.user.User
import ru.mephi.alumniclub.app.model.dto.form.request.AbstractFormRequest.FormBecomeMentorRequest
import ru.mephi.alumniclub.app.model.enumeration.FormType

@Component
class BecomeMentorFormMapper : AbstractFormMapper<FormBecomeMentor>() {
    override val type: FormType = FormType.BecomeMentor

    fun asEntity(author: User, request: FormBecomeMentorRequest): FormBecomeMentor {
        return FormBecomeMentor(
            company = request.company,
            position = request.position,
            expertArea = request.expertArea,
            whyAreYouMentor = request.whyAreYouMentor,
            helpArea = request.helpArea,
            timeForMentoring = request.timeForMentoring,
            formatsOfInteraction = request.formatsOfInteraction
        ).apply { this.author = author }
    }

    fun update(entity: FormBecomeMentor, request: FormBecomeMentorRequest): FormBecomeMentor {
        entity.company = request.company
        entity.position = request.position
        entity.expertArea = request.expertArea
        entity.whyAreYouMentor = request.whyAreYouMentor
        entity.helpArea = request.helpArea
        entity.timeForMentoring = request.timeForMentoring
        entity.formatsOfInteraction = request.formatsOfInteraction
        return entity
    }
}
