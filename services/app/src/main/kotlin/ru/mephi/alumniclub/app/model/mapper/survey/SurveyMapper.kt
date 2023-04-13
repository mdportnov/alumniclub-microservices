package ru.mephi.alumniclub.app.model.mapper.survey

import org.springframework.context.annotation.Lazy
import org.springframework.data.domain.Page
import org.springframework.stereotype.Component
import ru.mephi.alumniclub.app.database.entity.survey.Survey
import ru.mephi.alumniclub.app.database.entity.survey.SurveyItemAnswer
import ru.mephi.alumniclub.app.database.entity.survey.SurveyType
import ru.mephi.alumniclub.app.database.entity.survey.SurveyUserAnswer
import ru.mephi.alumniclub.app.model.dto.CursorResponse
import ru.mephi.alumniclub.app.model.dto.PageResponse
import ru.mephi.alumniclub.app.model.dto.survey.request.CreateSurveyRequest
import ru.mephi.alumniclub.app.model.dto.survey.request.SurveyItemAnswerRequest
import ru.mephi.alumniclub.app.model.dto.survey.request.UpdateSurveyRequest
import ru.mephi.alumniclub.app.model.dto.survey.response.*
import ru.mephi.alumniclub.app.model.mapper.user.UserMapper
import ru.mephi.alumniclub.app.service.SurveyService
import ru.mephi.alumniclub.app.service.UserService
import ru.mephi.alumniclub.shared.dto.common.ApiError
import java.util.*

@Component
class SurveyMapper(
    private val userMapper: UserMapper,
    @Lazy private val userService: UserService,
    @Lazy private val surveyService: SurveyService
) {
    fun asEntity(request: CreateSurveyRequest, userId: Long): Survey {
        val survey = Survey(
            title = request.title,
            content = request.content,
            endsAt = request.endsAt,
            allowCount = request.allowCount,
            baseAnswers = asEntities(request.baseAnswers),
            type = request.type,
        )
        survey.author = userService.findUserEntityById(userId)
        survey.baseAnswers.forEach { item -> item.survey = survey }
        return survey
    }

    fun asEntity(request: SurveyItemAnswerRequest): SurveyItemAnswer {
        return SurveyItemAnswer(answer = request.answer)
    }

    fun asEntities(requests: List<SurveyItemAnswerRequest>): MutableList<SurveyItemAnswer> =
        requests.map(::asEntity).toMutableList()

    fun asResponse(request: SurveyItemAnswer?) = request?.let {
        SurveyItemAnswerResponse(it.id, it.answer)
    }

    fun asResponses(requests: List<SurveyItemAnswer>) = requests.mapNotNull { asResponse(it) }.toMutableList()

    fun asSurveyResponseWithMetadata(entity: Survey, userId: Long? = null) = SurveyResponseWithMetadata(
        id = entity.id,
        createdAt = entity.createdAt,
        title = entity.title,
        content = entity.content,
        author = entity.author?.let { userMapper.asNullableShortResponse(it) },
        photoPath = entity.photoPath,
        endsAt = entity.endsAt,
        allowCount = entity.allowCount,
        baseAnswers = asResponses(entity.baseAnswers),
        type = entity.type,
        metadata = getSurveyMetadata(entity.id, userId)
    )

    fun asResponse(entity: Survey): SurveyResponse {
        return SurveyResponse(
            id = entity.id,
            createdAt = entity.createdAt,
            title = entity.title,
            content = entity.content,
            author = entity.author?.let { userMapper.asNullableShortResponse(it) },
            photoPath = entity.photoPath,
            endsAt = entity.endsAt,
            allowCount = entity.allowCount,
            baseAnswers = asResponses(entity.baseAnswers),
            type = entity.type
        )
    }

    fun asResponse(entity: SurveyUserAnswer): SurveyUserAnswerResponse {
        return SurveyUserAnswerResponse(
            id = entity.id,
            surveyId = entity.survey.id,
            surveyType = entitySurveyType(entity),
            user = userMapper.asShortResponse(userService.findUserEntityById(entity.answered.id)),
            selectedAnswers = asResponses(entity.selectedAnswers),
            freeAnswer = entityFreeAnswerFreeAnswer(entity)
        )
    }

    fun update(target: Survey, request: UpdateSurveyRequest) {
        with(target) {
            title = request.title
            content = request.content
            endsAt = request.endsAt
        }
    }

    fun asShortCursorResponse(page: Page<Survey>, userId: Long): CursorResponse<SurveyResponseWithMetadata> {
        return CursorResponse(
            content = page.content.map { asSurveyResponseWithMetadata(it, userId) },
            numberOfElements = page.numberOfElements.toLong()
        )
    }

    fun asShortPageResponse(page: Page<Survey>): PageResponse<SurveyResponseWithMetadata> {
        return PageResponse(
            content = page.content.map { asSurveyResponseWithMetadata(it) },
            number = page.number.toLong(),
            numberOfElements = page.numberOfElements.toLong(),
            totalPages = page.totalPages.toLong()
        )
    }

    fun asResponse(page: Page<SurveyUserAnswer>): PageResponse<SurveyUserAnswerResponse> {
        return PageResponse(
            content = page.content.map(::asResponse),
            number = page.number.toLong(),
            numberOfElements = page.numberOfElements.toLong(),
            totalPages = page.totalPages.toLong()
        )
    }

    fun getSurveyMetadata(surveyId: UUID, userId: Long?): SurveyMetadata {
        val info = surveyService.getSurveyAnswersInfo(surveyId)
        if (userId == null) return SurveyMetadata(info, null)
        val answer = try {
            surveyService.getUserAnswer(surveyId, userId)
        } catch (e: Throwable) {
            null
        }
        return SurveyMetadata(info, answer)
    }

    private fun entitySurveyType(surveyUserAnswer: SurveyUserAnswer): SurveyType {
        val survey = surveyUserAnswer.survey
        return survey.type
    }

    private fun entityFreeAnswerFreeAnswer(surveyUserAnswer: SurveyUserAnswer): String {
        val freeAnswer = surveyUserAnswer.freeAnswer
        return freeAnswer?.freeAnswer ?: ""
    }
}
