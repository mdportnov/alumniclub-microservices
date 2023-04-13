package ru.mephi.alumniclub.app.model.mapper.form;

import org.springframework.stereotype.Component
import ru.mephi.alumniclub.app.database.entity.atom.Merch
import ru.mephi.alumniclub.app.database.entity.form.FormBuyMerch
import ru.mephi.alumniclub.app.database.entity.user.User
import ru.mephi.alumniclub.app.model.enumeration.FormType

@Component
class BuyMerchFormMapper : AbstractFormMapper<FormBuyMerch>() {
    override val type: FormType = FormType.BuyMerch
    fun asEntity(author: User, merch: Merch): FormBuyMerch {
        return FormBuyMerch(
            merch = merch
        ).apply { this.author = author }
    }
}
