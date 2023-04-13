package ru.mephi.alumniclub.app.model.mapper.form;

import org.springframework.stereotype.Component
import ru.mephi.alumniclub.app.database.entity.form.FormOfferPoll
import ru.mephi.alumniclub.app.database.entity.user.User
import ru.mephi.alumniclub.app.model.dto.form.request.AbstractFormRequest.FormOfferPollRequest
import ru.mephi.alumniclub.app.model.enumeration.FormType

@Component
class OfferPollFormMapper : AbstractFormMapper<FormOfferPoll>() {
    override val type: FormType = FormType.OfferPoll

    fun asEntity(author: User, request: FormOfferPollRequest): FormOfferPoll {
        return FormOfferPoll(
            text = request.text
        ).apply { this.author = author }
    }
}
