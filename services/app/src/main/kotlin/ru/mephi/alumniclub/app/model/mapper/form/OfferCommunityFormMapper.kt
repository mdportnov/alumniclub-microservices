package ru.mephi.alumniclub.app.model.mapper.form;

import org.springframework.stereotype.Component
import ru.mephi.alumniclub.app.database.entity.form.FormOfferCommunity
import ru.mephi.alumniclub.app.database.entity.user.User
import ru.mephi.alumniclub.app.model.dto.form.request.AbstractFormRequest.FormOfferCommunityRequest
import ru.mephi.alumniclub.app.model.enumeration.FormType

@Component
class OfferCommunityFormMapper : AbstractFormMapper<FormOfferCommunity>() {
    override val type: FormType = FormType.OfferCommunity
    fun asEntity(author: User, request: FormOfferCommunityRequest): FormOfferCommunity {
        return FormOfferCommunity(
            text = request.text
        ).apply { this.author = author }
    }
}
