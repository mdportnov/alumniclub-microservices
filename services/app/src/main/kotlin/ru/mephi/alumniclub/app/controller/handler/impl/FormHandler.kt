package ru.mephi.alumniclub.app.controller.handler.impl

import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse
import org.springframework.web.servlet.function.ServerResponse.created
import org.springframework.web.servlet.function.ServerResponse.ok
import org.springframework.web.servlet.function.body
import ru.mephi.alumniclub.app.model.dto.form.request.AbstractFormRequest
import ru.mephi.alumniclub.app.model.dto.form.request.FormChangeStatusRequest
import ru.mephi.alumniclub.app.model.enumeration.FormType
import ru.mephi.alumniclub.app.service.FormService
import ru.mephi.alumniclub.shared.dto.common.ApiMessage
import ru.mephi.alumniclub.shared.dto.security.ScopePermission
import ru.mephi.alumniclub.shared.model.exceptions.common.MissingParameterException
import ru.mephi.alumniclub.shared.util.constants.API_VERSION_1
import ru.mephi.alumniclub.shared.util.constants.SERVER_NAME
import ru.mephi.alumniclub.shared.util.extension.*
import ru.mephi.alumniclub.shared.util.response.ResponseManager
import java.net.URI

@Component
class FormHandler(
    private var service: FormService
) : ResponseManager() {

    fun getSelfForms(request: ServerRequest): ServerResponse {
        return ok().contentType(MediaType.APPLICATION_JSON)
            .body(service.listAllUserForms(request.getPrincipal()))
    }

    fun getAllUserForms(request: ServerRequest): ServerResponse {
        request.assertHasOneOfPermission(ScopePermission.FORMS)
        val userId = request.param("id").orElseGet {
            throw MissingParameterException("id")
        }.toLong()
        return service.listAllUserForms(userId).toOkBody()
    }

    fun getFormsByType(request: ServerRequest): ServerResponse {
        request.assertHasOneOfPermission(ScopePermission.FORMS)
        val type = request.paramOrThrow<FormType>("type")
        val pageRequest = request.getExtendedPageRequest()
        val body = service.listByType(type, pageRequest)
        return body.toOkBody()
    }

    fun saveForm(request: ServerRequest): ServerResponse {
        val type = request.paramOrThrow<FormType>("type")

        val formRequest = request.body(
            when (type) {
                FormType.BuyMerch -> AbstractFormRequest.FormBuyMerchRequest::class.java
                FormType.BecomeMentor -> AbstractFormRequest.FormBecomeMentorRequest::class.java
                FormType.FindMentor -> AbstractFormRequest.FormFindMentorRequest::class.java
                FormType.OfferPartnership -> AbstractFormRequest.FormOfferPartnershipRequest::class.java
                FormType.JoinPartnership -> AbstractFormRequest.FormJoinPartnershipRequest::class.java
                FormType.OfferPoll -> AbstractFormRequest.FormOfferPollRequest::class.java
                FormType.OfferCommunity -> AbstractFormRequest.FormOfferCommunityRequest::class.java
            }
        )

        validate(formRequest)

        val form = service.saveForm(request.getPrincipal(), formRequest)
        val message = ApiMessage(data = form, message = i18n("label.form.send"))

        return created(URI("$SERVER_NAME$API_VERSION_1/form?type=$type&id=${form.id}")).body(message)
    }

    fun getFullForm(request: ServerRequest): ServerResponse {
        val userId = request.getPrincipal()

        val type = request.paramOrThrow<FormType>("type")

        val formId = request.param("id").orElseGet {
            throw MissingParameterException("id")
        }.toLong()

        return service.getFormByTypeAndUserId(type, formId, userId).toOkBody()
    }

    @ResponseBody
    fun changeStatus(request: ServerRequest): ServerResponse {
        request.assertHasOneOfPermission(ScopePermission.FORMS)
        val type = request.paramOrThrow<FormType>("type")

        val formId = request.pathVariableOrThrow<Long>("id")

        val newStatus = request.body<FormChangeStatusRequest>()

        return ApiMessage(
            data = service.changeStatus(type, formId, newStatus), message = i18n("label.form.statusChange")
        ).toOkBody()
    }

    fun delete(request: ServerRequest): ServerResponse {
        request.assertHasOneOfPermission(ScopePermission.FORMS)
        val type = request.paramOrThrow<FormType>("type")
        val id = request.pathVariableOrThrow<Long>("id")
        service.delete(type, id)
        return ApiMessage(data = null, message = i18n("label.common.deleted")).toOkBody()
    }

    fun listTypes(request: ServerRequest): ServerResponse {
        return service.listTypes().toOkBody()
    }

    fun getById(request: ServerRequest): ServerResponse {
        request.assertHasOneOfPermission(ScopePermission.FORMS)
        val type = request.paramOrThrow<FormType>("type")
        val id = request.pathVariableOrThrow<Long>("id")
        return service.getById(id, type).toOkBody()
    }
}
