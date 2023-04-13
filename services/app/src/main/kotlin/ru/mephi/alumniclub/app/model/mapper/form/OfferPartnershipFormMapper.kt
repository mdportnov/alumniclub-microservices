package ru.mephi.alumniclub.app.model.mapper.form;

import org.springframework.stereotype.Component
import ru.mephi.alumniclub.app.database.entity.form.FormOfferPartnership
import ru.mephi.alumniclub.app.database.entity.user.User
import ru.mephi.alumniclub.app.model.dto.form.request.AbstractFormRequest.FormOfferPartnershipRequest
import ru.mephi.alumniclub.app.model.enumeration.FormType

@Component
class OfferPartnershipFormMapper : AbstractFormMapper<FormOfferPartnership>() {
    override val type: FormType = FormType.OfferPartnership
    fun asEntity(author: User, request: FormOfferPartnershipRequest): FormOfferPartnership {
        return FormOfferPartnership(
            requirements = request.requirements,
            selfDescription = request.selfDescription,
            name = request.projectName,
            currentUntil = request.currentUntil,
            projectDescription = request.projectDescription,
            helpDescription = request.helpDescription
        ).apply { this.author = author }
    }
}
