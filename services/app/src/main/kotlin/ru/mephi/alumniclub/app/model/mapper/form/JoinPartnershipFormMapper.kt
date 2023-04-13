package ru.mephi.alumniclub.app.model.mapper.form;

import org.springframework.stereotype.Component
import ru.mephi.alumniclub.app.database.entity.form.FormJoinPartnership
import ru.mephi.alumniclub.app.database.entity.partnership.Partnership
import ru.mephi.alumniclub.app.database.entity.user.User
import ru.mephi.alumniclub.app.model.dto.form.request.AbstractFormRequest.FormJoinPartnershipRequest
import ru.mephi.alumniclub.app.model.enumeration.FormType

@Component
class JoinPartnershipFormMapper : AbstractFormMapper<FormJoinPartnership>() {
    override val type: FormType = FormType.JoinPartnership
    fun asEntity(author: User, partnership: Partnership, request: FormJoinPartnershipRequest): FormJoinPartnership {
        return FormJoinPartnership(
            contribution = request.contribution
        ).apply {
            this.author = author
            this.partnership = partnership
        }
    }
}
