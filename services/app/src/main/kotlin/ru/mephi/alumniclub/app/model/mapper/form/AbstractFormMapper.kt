package ru.mephi.alumniclub.app.model.mapper.form

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import ru.mephi.alumniclub.app.database.entity.form.AbstractFormEntity
import ru.mephi.alumniclub.app.model.dto.PageResponse
import ru.mephi.alumniclub.app.model.dto.form.response.FormResponse
import ru.mephi.alumniclub.app.model.dto.form.response.FormShortResponse
import ru.mephi.alumniclub.app.model.dto.form.response.SelfFormResponse
import ru.mephi.alumniclub.app.model.enumeration.FormType
import ru.mephi.alumniclub.app.model.mapper.user.UserMapper

abstract class AbstractFormMapper<T : AbstractFormEntity> {
    @Autowired
    lateinit var userMapper: UserMapper

    abstract val type: FormType

    fun asFullResponse(entity: T): FormResponse {
        return FormResponse(
            id = entity.id,
            createdAt = entity.createdAt,
            author = userMapper.asShortResponse(entity.author!!),
            status = entity.status,
            type = type,
            answers = entity.getFieldNameAndValueList()
        )
    }

    fun asShortResponseList(forms: Page<T>): PageResponse<FormShortResponse> {
        return PageResponse(
            content = forms.content.map(::asShortResponse),
            number = forms.number.toLong(),
            numberOfElements = forms.numberOfElements.toLong(),
            totalPages = forms.totalPages.toLong()
        )
    }

    fun asSelfFormResponseList(forms: List<T>) = forms.map(::asSelfFormResponse)

    private fun asShortResponse(entity: T): FormShortResponse {
        return FormShortResponse(
            id = entity.id,
            createdAt = entity.createdAt,
            author = userMapper.asShortResponse(entity.author!!),
            status = entity.status,
            type = type
        )
    }

    private fun asSelfFormResponse(entity: T): SelfFormResponse {
        return SelfFormResponse(
            id = entity.id,
            createdAt = entity.createdAt,
            type = type
        )
    }
}