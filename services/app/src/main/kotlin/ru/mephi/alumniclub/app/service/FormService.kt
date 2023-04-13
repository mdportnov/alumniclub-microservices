package ru.mephi.alumniclub.app.service

import io.swagger.v3.oas.annotations.Parameter
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import ru.mephi.alumniclub.app.model.dto.PageResponse
import ru.mephi.alumniclub.app.model.dto.form.request.AbstractFormRequest
import ru.mephi.alumniclub.app.model.dto.form.request.FormChangeStatusRequest
import ru.mephi.alumniclub.app.model.dto.form.response.FormResponse
import ru.mephi.alumniclub.app.model.dto.form.response.FormShortResponse
import ru.mephi.alumniclub.app.model.dto.form.response.FormTypeResponse
import ru.mephi.alumniclub.app.model.dto.form.response.SelfFormResponse
import ru.mephi.alumniclub.app.model.enumeration.FormType
import ru.mephi.alumniclub.shared.dto.ExtendedPageRequest

interface FormService {
    fun listAllUserForms(@RequestParam("id") userId: Long): List<SelfFormResponse>
    fun listByType(
        @RequestParam("type") formType: FormType,
        pageRequest: ExtendedPageRequest
    ): PageResponse<FormShortResponse>

    fun saveForm(@Parameter(hidden = true) userId: Long, @RequestBody request: AbstractFormRequest): FormResponse
    fun listTypes(): List<FormTypeResponse>
    fun getById(formId: Long, formType: FormType): FormResponse
    fun changeStatus(
        @RequestParam("type") formType: FormType,
        @RequestParam("id") formId: Long,
        @RequestBody newStatus: FormChangeStatusRequest
    ): FormResponse

    fun getFormByTypeAndUserId(
        @RequestParam("type") formType: FormType,
        @RequestParam("id") formId: Long,
        @Parameter(hidden = true) userId: Long
    ): FormResponse

    fun delete(@RequestParam("type") formType: FormType, @RequestParam("id") formId: Long)
}
