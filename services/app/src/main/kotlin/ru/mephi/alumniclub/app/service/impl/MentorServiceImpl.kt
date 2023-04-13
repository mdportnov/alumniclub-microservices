package ru.mephi.alumniclub.app.service.impl

import org.springframework.data.jpa.repository.Modifying
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import ru.mephi.alumniclub.app.database.entity.user.MentorData
import ru.mephi.alumniclub.app.database.repository.user.MentorDao
import ru.mephi.alumniclub.app.model.dto.PageResponse
import ru.mephi.alumniclub.app.model.dto.mentor.request.CreateMentorRequest
import ru.mephi.alumniclub.app.model.dto.mentor.request.ToggleMentorAvailabilityRequest
import ru.mephi.alumniclub.app.model.dto.mentor.request.UpdateMentorRequest
import ru.mephi.alumniclub.app.model.dto.mentor.response.MentorFullResponse
import ru.mephi.alumniclub.app.model.dto.mentor.response.MentorShortResponse
import ru.mephi.alumniclub.app.model.enumeration.user.Role
import ru.mephi.alumniclub.app.model.exception.common.ResourceNotFoundException
import ru.mephi.alumniclub.app.model.mapper.user.MentorMapper
import ru.mephi.alumniclub.app.service.MentorService
import ru.mephi.alumniclub.app.service.UserService
import ru.mephi.alumniclub.shared.dto.ExtendedPageRequest
import ru.mephi.alumniclub.shared.dto.common.ApiError
import ru.mephi.alumniclub.shared.util.response.ResponseManager
import javax.transaction.Transactional

@Service
class MentorServiceImpl(
    private val mentorDao: MentorDao,
    private val mentorMapper: MentorMapper,
    private var userService: UserService,
) : ResponseManager(), MentorService {

    @Transactional
    override fun listAllMentors(
        query: String,
        extendedPageRequest: ExtendedPageRequest
    ): PageResponse<MentorShortResponse> {
        val mentors = mentorDao.findByUserSurnameStartsWithOrderByAvailableDesc(query, extendedPageRequest.pageable)
        return mentorMapper.asShortPageResponse(mentors)
    }

    @Transactional
    override fun listAllAvailableMentors(
        query: String,
        extendedPageRequest: ExtendedPageRequest
    ): PageResponse<MentorShortResponse> {
        val mentors = mentorDao.findByUserSurnameStartsWithAndAvailableIsTrue(query, extendedPageRequest.pageable)
        return mentorMapper.asShortPageResponse(mentors)
    }

    @Transactional
    override fun toggleMentorAvailability(userId: Long, request: ToggleMentorAvailabilityRequest): MentorFullResponse {
        val mentor = getMentorEntityByUserId(userId)
        mentor.available = request.available
        return mentorMapper.asFullResponse(mentor)
    }

    @Transactional
    override fun saveUserMentorInfo(request: CreateMentorRequest): MentorFullResponse {
        val user = userService.findUserEntityById(request.id)
        if (mentorDao.existsByUserId(request.id)) {
            throw ApiError(HttpStatus.CONFLICT, i18n("exception.alreadyExists.mentor", request.id.toString()))
        }
        userService.updateRoles(user, user.roles + Role.MENTOR)
        val entity = mentorMapper.asEntity(user, request).apply { available = true }
        mentorDao.save(entity)
        return mentorMapper.asFullResponse(getMentorEntityByUserId(request.id))
    }

    @Transactional
    override fun updateUserMentorInfo(userId: Long, request: UpdateMentorRequest): MentorFullResponse {
        val mentorData = getMentorEntityByUserId(userId)
        return mentorMapper.update(mentorData, request, userId).let { mentorMapper.asFullResponse(it) }
    }

    @Modifying
    @Transactional
    override fun deleteUserMentor(userId: Long) {
        if (!mentorDao.existsByUserId(userId)) {
            throw ResourceNotFoundException(MentorData::class.java, userId)
        }
        val user = userService.findUserEntityById(userId)
        userService.updateRoles(user, user.roles - Role.MENTOR)
        mentorDao.deleteByUserId(user.id)
    }

    @Transactional
    override fun getMentorByUserId(userId: Long, includeNotAvailable: Boolean): MentorFullResponse {
        val mentor = getMentorEntityByUserId(userId)
        if (!includeNotAvailable && !mentor.available) throw ResourceNotFoundException(MentorData::class.java, userId)
        return mentorMapper.asFullResponse(mentor)
    }

    private fun getMentorEntityByUserId(userId: Long): MentorData {
        return mentorDao.findByUserId(userId) ?: throw ResourceNotFoundException(MentorData::class.java, userId)
    }
}
