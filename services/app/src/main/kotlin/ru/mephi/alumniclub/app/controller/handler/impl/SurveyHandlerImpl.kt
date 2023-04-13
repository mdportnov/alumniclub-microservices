package ru.mephi.alumniclub.app.controller.handler.impl

import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse
import org.springframework.web.servlet.function.body
import ru.mephi.alumniclub.app.controller.handler.SurveyHandler
import ru.mephi.alumniclub.app.model.dto.survey.request.CreateSurveyRequest
import ru.mephi.alumniclub.app.model.dto.survey.request.UpdateSurveyRequest
import ru.mephi.alumniclub.app.model.dto.survey.request.VoteSurveyRequest
import ru.mephi.alumniclub.app.service.SurveyService
import ru.mephi.alumniclub.shared.dto.common.ApiError
import ru.mephi.alumniclub.shared.dto.common.ApiMessage
import ru.mephi.alumniclub.shared.dto.security.ScopePermission
import ru.mephi.alumniclub.shared.util.extension.*
import ru.mephi.alumniclub.shared.util.response.ResponseManager
import java.util.*

@Service
class SurveyHandlerImpl(
    private val surveyService: SurveyService
) : ResponseManager(), SurveyHandler {

    override fun create(request: ServerRequest): ServerResponse {
        request.assertHasOneOfPermission(ScopePermission.SURVEYS)
        val userId = request.getPrincipal()
        val createRequest = request.body<CreateSurveyRequest>()
        validate(createRequest)
        val response = surveyService.create(createRequest, userId)
        return ApiMessage(message = i18n("label.common.created"), response).toOkBody()
    }

    override fun vote(request: ServerRequest): ServerResponse {
        val surveyId = request.pathVariableOrThrow<UUID>("id")
        val userId = request.getPrincipal()
        val voteSurvey = request.body<VoteSurveyRequest>()
        validate(request)
        return ApiMessage(
            data = surveyService.voteInSurvey(voteSurvey, userId, surveyId),
            message = i18n("label.survey.vote")
        ).toOkBody()
    }

    override fun update(request: ServerRequest): ServerResponse {
        request.assertHasOneOfPermission(ScopePermission.SURVEYS)
        val surveyId = request.pathVariableOrThrow<UUID>("id")
        val updateRequest = request.body<UpdateSurveyRequest>()
        validate(updateRequest)
        val response = surveyService.updateSurvey(updateRequest, surveyId)
        return ApiMessage(message = i18n("label.common.updated"), response).toOkBody()
    }

    override fun uploadPhoto(request: ServerRequest): ServerResponse {
        request.assertHasOneOfPermission(ScopePermission.SURVEYS)
        val surveyId = request.pathVariableOrThrow<UUID>("id")
        val file = request.getMultiPartPhoto()
            ?: throw ApiError(HttpStatus.BAD_REQUEST, i18n("exception.media.invalidMultipart"))
        val publication = surveyService.uploadPhoto(surveyId, file)
        return ApiMessage(message = i18n("label.common.updated"), publication).toOkBody()
    }

    override fun getById(request: ServerRequest): ServerResponse {
        val userId = request.getPrincipal()
        val id = request.pathVariableOrThrow<UUID>("id")
        val response = surveyService.getSurveyById(id, userId)
        return response.toOkBody()
    }

    override fun pageList(request: ServerRequest): ServerResponse {
        request.assertHasOneOfPermission(ScopePermission.SURVEYS)
        val page = request.getExtendedPageRequest()
        val response = surveyService.list(page)
        return response.toOkBody()
    }

    override fun cursorList(request: ServerRequest): ServerResponse {
        val userId = request.getPrincipal()
        val cursor = request.getCursorRequest()
        val includeFinished = request.paramOrElse("includeFinished") { true }
        val response = surveyService.list(cursor, userId, includeFinished)
        return response.toOkBody()
    }

    override fun getSelfAnswer(request: ServerRequest): ServerResponse {
        val userId = request.getPrincipal()
        val surveyId = request.pathVariableOrThrow<UUID>("id")
        val response = surveyService.getUserAnswer(surveyId, userId)
        return response.toOkBody()
    }

    override fun getAnswerOfUser(request: ServerRequest): ServerResponse {
        request.assertHasOneOfPermission(ScopePermission.SURVEYS)
        val userId = request.pathVariableOrThrow<Long>("userId")
        val surveyId = request.pathVariableOrThrow<UUID>("id")
        val response = surveyService.getUserAnswer(surveyId, userId)
        return response.toOkBody()
    }

    override fun getUsersByVariant(request: ServerRequest): ServerResponse {
        request.assertHasOneOfPermission(ScopePermission.SURVEYS)
        val surveyId = request.pathVariableOrThrow<UUID>("id")
        val variantId = request.pathVariableOrThrow<Long>("variantId")
        val page = request.getExtendedPageRequest()
        val response = surveyService.getUsersByVariantId(surveyId, variantId, page)
        return response.toOkBody()
    }

    override fun getAnswersList(request: ServerRequest): ServerResponse {
        val surveyId = request.pathVariableOrThrow<UUID>("id")
        val pageRequest = request.getExtendedPageRequest()
        val response = surveyService.getAnswersList(surveyId, pageRequest)
        return response.toOkBody()
    }

    override fun getSurveyAnswersInfo(request: ServerRequest): ServerResponse {
        val surveyId = request.pathVariableOrThrow<UUID>("id")
        val response = surveyService.getSurveyAnswersInfo(surveyId)
        return response.toOkBody()
    }

    override fun getSurveyMetadata(request: ServerRequest): ServerResponse {
        val userId = request.getPrincipal()
        val surveyId = request.pathVariableOrThrow<UUID>("id")
        val response = surveyService.getSurveyMetadata(surveyId, userId)
        return response.toOkBody()
    }

    override fun delete(request: ServerRequest): ServerResponse {
        request.assertHasOneOfPermission(ScopePermission.SURVEYS)
        val surveyId = request.pathVariableOrThrow<UUID>("id")
        surveyService.remove(surveyId)
        return ApiMessage(data = null, message = i18n("label.common.deleted")).toOkBody()
    }
}
