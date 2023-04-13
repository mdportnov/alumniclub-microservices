package ru.mephi.alumniclub.app.database.repository.form

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import ru.mephi.alumniclub.app.database.entity.form.FormJoinPartnership
import ru.mephi.alumniclub.app.model.enumeration.FormStatus

interface FormJoinPartnershipDao : AbstractFormDao<FormJoinPartnership> {
    fun findByPartnershipIdAndStatus(
        id: Long, pageable: Pageable, status: FormStatus = FormStatus.ACCEPTED
    ): Page<FormJoinPartnership>
}
