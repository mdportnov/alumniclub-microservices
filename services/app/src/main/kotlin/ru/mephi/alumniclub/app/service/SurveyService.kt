package ru.mephi.alumniclub.app.service

import io.swagger.v3.oas.annotations.Parameter
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import ru.mephi.alumniclub.app.database.entity.survey.Survey
import ru.mephi.alumniclub.app.database.entity.survey.SurveyItemAnswer
import ru.mephi.alumniclub.app.model.dto.CursorResponse
import ru.mephi.alumniclub.app.model.dto.PageResponse
import ru.mephi.alumniclub.app.model.dto.survey.request.CreateSurveyRequest
import ru.mephi.alumniclub.app.model.dto.survey.request.UpdateSurveyRequest
import ru.mephi.alumniclub.app.model.dto.survey.request.VoteSurveyRequest
import ru.mephi.alumniclub.app.model.dto.survey.response.*
import ru.mephi.alumniclub.app.model.dto.user.response.UserPreviewResponse
import ru.mephi.alumniclub.shared.dto.ExtendedPageRequest
import ru.mephi.alumniclub.shared.util.Cursor
import java.util.*
import javax.servlet.http.Part

interface SurveyService {
    fun create(@RequestBody request: CreateSurveyRequest, @Parameter(hidden = true) userId: Long): SurveyResponse
    fun remove(@PathVariable("id") surveyId: UUID)
    fun getSurveyById(
        @PathVariable("id") id: UUID,
        @Parameter(hidden = true) userId: Long
    ): SurveyResponseWithMetadata

    fun list(@RequestBody page: ExtendedPageRequest): PageResponse<SurveyResponseWithMetadata>
    fun list(
        @RequestBody cursor: Cursor,
        @Parameter(hidden = true) userId: Long,
        @RequestParam includeFinished: Boolean
    ): CursorResponse<SurveyResponseWithMetadata>

    fun getUserAnswer(
        @PathVariable("id") surveyId: UUID,
        @PathVariable("userId") userId: Long
    ): SurveyUserAnswerResponse

    fun getAnswersList(
        @PathVariable("id") surveyId: UUID,
        @RequestBody pageRequest: ExtendedPageRequest
    ): PageResponse<SurveyUserAnswerResponse>

    fun getUsersByVariantId(
        surveyId: UUID,
        variantId: Long,
        page: ExtendedPageRequest
    ): PageResponse<UserPreviewResponse>

    fun getSurveyAnswersInfo(@PathVariable("id") surveyId: UUID): SurveyAnswersInfoResponse
    fun updateSurvey(@RequestBody request: UpdateSurveyRequest, @PathVariable("id") id: UUID): SurveyResponse
    fun voteInSurvey(
        @RequestBody request: VoteSurveyRequest,
        @Parameter(hidden = true) userId: Long,
        @PathVariable("id") surveyId: UUID
    ): SurveyMetadata

    fun getSurveyMetadata(surveyId: UUID, userId: Long?): SurveyMetadata

    fun uploadPhoto(@PathVariable("id") surveyId: UUID, file: Part): SurveyResponse

    fun findEntityById(id: UUID): Survey
    fun findEntityOrNullById(id: UUID): Survey?
    fun findItemAnswerById(id: Long): SurveyItemAnswer
}
