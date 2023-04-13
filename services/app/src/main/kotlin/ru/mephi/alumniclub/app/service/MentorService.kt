package ru.mephi.alumniclub.app.service

import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import ru.mephi.alumniclub.app.model.dto.PageResponse
import ru.mephi.alumniclub.app.model.dto.mentor.*
import ru.mephi.alumniclub.app.model.dto.mentor.request.CreateMentorRequest
import ru.mephi.alumniclub.app.model.dto.mentor.request.ToggleMentorAvailabilityRequest
import ru.mephi.alumniclub.app.model.dto.mentor.request.UpdateMentorRequest
import ru.mephi.alumniclub.app.model.dto.mentor.response.MentorFullResponse
import ru.mephi.alumniclub.app.model.dto.mentor.response.MentorShortResponse
import ru.mephi.alumniclub.shared.dto.ExtendedPageRequest

interface MentorService {
    fun listAllMentors(query: String, extendedPageRequest: ExtendedPageRequest): PageResponse<MentorShortResponse>
    fun saveUserMentorInfo(@RequestBody request: CreateMentorRequest): MentorFullResponse
    fun deleteUserMentor(@PathVariable("id") userId: Long)
    fun getMentorByUserId(@PathVariable("id") userId: Long, includeNotAvailable: Boolean = false): MentorFullResponse
    fun toggleMentorAvailability(
        @PathVariable("id") userId: Long,
        @RequestBody request: ToggleMentorAvailabilityRequest
    ): MentorFullResponse

    fun updateUserMentorInfo(
        @PathVariable("id") userId: Long,
        @RequestBody request: UpdateMentorRequest
    ): MentorFullResponse

    fun listAllAvailableMentors(
        query: String,
        extendedPageRequest: ExtendedPageRequest
    ): PageResponse<MentorShortResponse>
}
