package ru.mephi.alumniclub.app.service.impl

import org.slf4j.LoggerFactory
import org.springframework.cache.annotation.CacheEvict
import org.springframework.context.annotation.Lazy
import org.springframework.data.jpa.repository.Modifying
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import ru.mephi.alumniclub.app.database.entity.survey.*
import ru.mephi.alumniclub.app.database.entity.user.User
import ru.mephi.alumniclub.app.database.repository.survey.*
import ru.mephi.alumniclub.app.model.dto.CursorResponse
import ru.mephi.alumniclub.app.model.dto.PageResponse
import ru.mephi.alumniclub.app.model.dto.survey.request.CreateSurveyRequest
import ru.mephi.alumniclub.app.model.dto.survey.request.UpdateSurveyRequest
import ru.mephi.alumniclub.app.model.dto.survey.request.VoteSurveyRequest
import ru.mephi.alumniclub.app.model.dto.survey.response.*
import ru.mephi.alumniclub.app.model.dto.user.response.UserPreviewResponse
import ru.mephi.alumniclub.app.model.exception.common.ResourceNotFoundException
import ru.mephi.alumniclub.app.model.mapper.broadcast.BroadcastSenderMapper
import ru.mephi.alumniclub.app.model.mapper.survey.SurveyMapper
import ru.mephi.alumniclub.app.model.mapper.user.UserMapper
import ru.mephi.alumniclub.app.service.*
import ru.mephi.alumniclub.shared.dto.ExtendedPageRequest
import ru.mephi.alumniclub.shared.dto.common.ApiError
import ru.mephi.alumniclub.shared.dto.photo.ContentPhotoRequest
import ru.mephi.alumniclub.shared.model.enums.StoreDir
import ru.mephi.alumniclub.shared.util.Cursor
import ru.mephi.alumniclub.shared.util.constants.LIKES_COUNT_CACHE
import ru.mephi.alumniclub.shared.util.response.ResponseManager
import java.time.LocalDateTime
import java.util.*
import javax.servlet.http.Part
import javax.transaction.Transactional
import kotlin.jvm.optionals.getOrNull

@Service
class SurveyServiceImpl(
    @Lazy private val broadcastService: BroadcastSenderService,
    private val userService: UserService,
    private val storageManager: StorageManager,
    private val mapper: SurveyMapper,
    private val broadcastMapper: BroadcastSenderMapper,
    private val userMapper: UserMapper,
    private val dao: SurveyDao,
    private val itemAnswerDao: SurveyItemAnswerDao,
    private val userAnswerDao: SurveyUserAnswerDao,
    private val freeAnswerDao: SurveyFreeAnswerDao,
    private val selectedAnswerDao: SurveySelectedAnswersDao,
    private val contentPhotoService: ContentPhotoService,
) : ResponseManager(), SurveyService {

    private val logger = LoggerFactory.getLogger(SurveyServiceImpl::class.java)

    @Transactional
    override fun create(request: CreateSurveyRequest, userId: Long): SurveyResponse {
        val survey = mapper.asEntity(request, userId)
        dao.save(survey)
        contentPhotoService.queueContent(ContentPhotoRequest(survey.id, survey.content))
        broadcastService.createBroadcast(userId, broadcastMapper.asBroadcastByPublication(survey, request.broadcast))
        return mapper.asResponse(survey)
    }

    @Modifying
    @Transactional
    override fun updateSurvey(request: UpdateSurveyRequest, id: UUID): SurveyResponse {
        val entity = findEntityById(id)
        mapper.update(entity, request)
        contentPhotoService.queueContent(ContentPhotoRequest(entity.id, entity.content))
        return mapper.asResponse(entity)
    }

    @CacheEvict(value = [LIKES_COUNT_CACHE], key = "#surveyId")
    @Modifying
    @Transactional
    override fun remove(surveyId: UUID) {
        val survey = findEntityById(surveyId)
        dao.deleteById(survey.id)
        storageManager.removeImage(survey.photoPath, StoreDir.SURVEY)
    }

    override fun uploadPhoto(surveyId: UUID, file: Part): SurveyResponse {
        val survey = findEntityById(surveyId)
        survey.photoPath = storageManager.saveImage(file, StoreDir.SURVEY, survey.photoPath, true)
        return mapper.asResponse(survey)
    }

    override fun getSurveyById(id: UUID, userId: Long): SurveyResponseWithMetadata {
        val survey = findEntityById(id)
        return mapper.asSurveyResponseWithMetadata(survey, userId)
    }

    @Transactional
    override fun list(page: ExtendedPageRequest): PageResponse<SurveyResponseWithMetadata> {
        val list = dao.findAll(page.pageable)
        return mapper.asShortPageResponse(list)
    }

    @Transactional
    override fun list(
        cursor: Cursor, userId: Long, includeFinished: Boolean
    ): CursorResponse<SurveyResponseWithMetadata> {
        val endsAt = if (includeFinished) LocalDateTime.of(0, 1, 1, 0, 0) else LocalDateTime.now()
        val list = when (cursor.chronology) {
            Cursor.Chronology.BEFORE -> dao.findByCreatedAtBeforeAndEndsAtAfter(cursor.from, endsAt, cursor.page)
            Cursor.Chronology.AFTER -> dao.findByCreatedAtAfterAndEndsAtAfter(cursor.from, endsAt, cursor.page)
        }
        return mapper.asShortCursorResponse(list, userId)
    }

    override fun findEntityById(id: UUID): Survey {
        return dao.findById(id).orElseThrow { ResourceNotFoundException(Survey::class.java, id) }
    }

    @OptIn(ExperimentalStdlibApi::class)
    override fun findEntityOrNullById(id: UUID): Survey? {
        return dao.findById(id).getOrNull()
    }

    @Modifying
    @Transactional
    override fun voteInSurvey(
        request: VoteSurveyRequest, userId: Long, surveyId: UUID
    ): SurveyMetadata {
        val survey = findEntityById(surveyId)
        if (surveyIsEnd(survey))
            throw ApiError(HttpStatus.FORBIDDEN, i18n("exception.survey.ended", survey.endsAt.toLocalDate().toString()))
        val user = userService.findUserEntityById(userId)
        if (userAlreadyVoted(user, survey))
            throw ApiError(HttpStatus.FORBIDDEN, i18n("exception.alreadyExists.vote"))
        when (survey.type) {
            SurveyType.CHOICE -> handleChoiceTypeAnswer(request, survey, user)
            SurveyType.FREE_FORM -> handleFreeformTypeAnswer(request, survey, user)
        }
        return mapper.getSurveyMetadata(surveyId, userId)
    }

    override fun getSurveyMetadata(surveyId: UUID, userId: Long?): SurveyMetadata {
        return mapper.getSurveyMetadata(surveyId, userId)
    }

    private fun handleChoiceTypeAnswer(request: VoteSurveyRequest, survey: Survey, user: User) {
        val selectedAnswers = request.selectedAnswers.map { findItemAnswerById(it) }.toMutableList()
        if (selectedAnswers.isEmpty())
            throw ApiError(HttpStatus.FORBIDDEN, i18n("exception.survey.emptyAnswer"))
        if (selectedAnswers.size > survey.allowCount)
            throw ApiError(HttpStatus.FORBIDDEN, i18n("exception.survey.tooMuchAnswers"))
        val answer = userAnswerDao.save(SurveyUserAnswer(survey, user))
        selectedAnswers.forEach {
            answer.surveySelectedAnswers.add(selectedAnswerDao.save(SurveySelectedAnswers(answer, it)))
        }
    }

    private fun handleFreeformTypeAnswer(request: VoteSurveyRequest, survey: Survey, user: User) {
        if (request.freeAnswer.isNullOrEmpty())
            throw ApiError(HttpStatus.FORBIDDEN, i18n("exception.survey.emptyAnswer"))
        val answer = userAnswerDao.save(SurveyUserAnswer(survey, user))
        freeAnswerDao.save(SurveyFreeAnswer(answer, request.freeAnswer))
    }

    override fun findItemAnswerById(id: Long): SurveyItemAnswer {
        return itemAnswerDao.findById(id).orElseThrow { ResourceNotFoundException(SurveyItemAnswer::class.java, id) }
    }

    @Transactional
    override fun getSurveyAnswersInfo(surveyId: UUID): SurveyAnswersInfoResponse {
        val survey = findEntityById(surveyId)
        val votesInfo = when (survey.type) {
            SurveyType.CHOICE -> survey.baseAnswers.map {
                val votes = userAnswerDao.getCountVotesOfItemAnswer(it.id)
                ItemAnswerInfoResponse(it.id, votes)
            }

            SurveyType.FREE_FORM -> listOf()
        }
        val allVotesCount = userAnswerDao.countAllBySurvey(survey)
        return SurveyAnswersInfoResponse(survey.id, allVotesCount, votesInfo)
    }

    @Transactional
    override fun getUserAnswer(surveyId: UUID, userId: Long): SurveyUserAnswerResponse {
        val user = userService.findUserEntityById(userId)
        val survey = findEntityById(surveyId)
        val answer = userAnswerDao.findByAnsweredAndSurvey(user, survey)
            .orElseThrow { ApiError(HttpStatus.NOT_FOUND, i18n("exception.survey.notVoted")) }
        return mapper.asResponse(answer)
    }

    @Transactional
    override fun getAnswersList(
        surveyId: UUID, pageRequest: ExtendedPageRequest
    ): PageResponse<SurveyUserAnswerResponse> {
        val survey = findEntityById(surveyId)
        val page = userAnswerDao.findAllBySurvey(survey, pageRequest.pageable)
        return mapper.asResponse(page)
    }

    @Transactional
    override fun getUsersByVariantId(
        surveyId: UUID, variantId: Long, page: ExtendedPageRequest
    ): PageResponse<UserPreviewResponse> {
        val survey = findEntityById(surveyId)
        if (survey.type != SurveyType.CHOICE)
            throw ApiError(HttpStatus.NOT_FOUND, i18n("exception.notFound.surveyAnswer"))
        if (survey.baseAnswers.find { it.id == variantId } == null)
            throw ApiError(HttpStatus.NOT_FOUND, i18n("exception.notFound.surveyAnswer"))
        val userPage = userAnswerDao.findUsersBySurveyAndItemAnswerId(surveyId, variantId, page.pageable)
        return userMapper.asUserPreviewPageResponse(userPage)
    }

    private fun surveyIsEnd(survey: Survey): Boolean {
        return survey.endsAt.isBefore(LocalDateTime.now())
    }

    private fun userAlreadyVoted(user: User, survey: Survey): Boolean {
        return userAnswerDao.existsByAnsweredAndSurvey(user, survey)
    }
}
