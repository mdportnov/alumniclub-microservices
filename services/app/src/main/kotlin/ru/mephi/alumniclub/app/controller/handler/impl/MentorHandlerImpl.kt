package ru.mephi.alumniclub.app.controller.handler.impl

import org.springframework.stereotype.Component
import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse
import org.springframework.web.servlet.function.ServerResponse.created
import org.springframework.web.servlet.function.body
import ru.mephi.alumniclub.app.controller.handler.MentorHandler
import ru.mephi.alumniclub.app.model.dto.mentor.request.CreateMentorRequest
import ru.mephi.alumniclub.app.model.dto.mentor.request.ToggleMentorAvailabilityRequest
import ru.mephi.alumniclub.app.model.dto.mentor.request.UpdateMentorRequest
import ru.mephi.alumniclub.app.service.MentorService
import ru.mephi.alumniclub.shared.dto.common.ApiMessage
import ru.mephi.alumniclub.shared.dto.security.ScopePermission
import ru.mephi.alumniclub.shared.util.constants.API_VERSION_1
import ru.mephi.alumniclub.shared.util.constants.SERVER_NAME
import ru.mephi.alumniclub.shared.util.extension.assertHasOneOfPermission
import ru.mephi.alumniclub.shared.util.extension.getExtendedPageRequest
import ru.mephi.alumniclub.shared.util.extension.paramOrElse
import ru.mephi.alumniclub.shared.util.extension.toOkBody
import ru.mephi.alumniclub.shared.util.response.ResponseManager
import java.net.URI

@Component
class MentorHandlerImpl(
    private val service: MentorService
) : ResponseManager(), MentorHandler {
    override fun listAll(request: ServerRequest): ServerResponse {
        val pageRequest = request.getExtendedPageRequest()
        val query = request.paramOrElse("query") { "" }.trim()
        return service.listAllMentors(query, pageRequest).toOkBody()
    }

    override fun listAllAvailable(request: ServerRequest): ServerResponse {
        val pageRequest = request.getExtendedPageRequest()
        val query = request.paramOrElse("query") { "" }.trim()
        return service.listAllAvailableMentors(query, pageRequest).toOkBody()
    }

    override fun toggleMentorAvailability(request: ServerRequest): ServerResponse {
        request.assertHasOneOfPermission(ScopePermission.MENTORS)
        val availability = request.body<ToggleMentorAvailabilityRequest>()
        val userId = request.pathVariable("id").toLong()
        return ApiMessage(
            data = service.toggleMentorAvailability(userId, availability),
            message = i18n("label.common.updated")
        ).toOkBody()
    }

    override fun getByIdForPublic(request: ServerRequest): ServerResponse {
        val userId = request.pathVariable("id").toLong()
        return service.getMentorByUserId(userId, includeNotAvailable = false).toOkBody()
    }

    override fun getByIdForAdmin(request: ServerRequest): ServerResponse {
        val userId = request.pathVariable("id").toLong()
        return service.getMentorByUserId(userId, includeNotAvailable = true).toOkBody()
    }

    override fun create(request: ServerRequest): ServerResponse {
        request.assertHasOneOfPermission(ScopePermission.MENTORS)
        val mentorData = request.body<CreateMentorRequest>()
        validate(mentorData)
        val message = ApiMessage(message = i18n("label.common.created"), service.saveUserMentorInfo(mentorData))
        return created(URI("$SERVER_NAME$API_VERSION_1/admin/mentor/${mentorData.id}")).body(message)
    }

    override fun update(request: ServerRequest): ServerResponse {
        request.assertHasOneOfPermission(ScopePermission.MENTORS)
        val mentorData = request.body<UpdateMentorRequest>()
        validate(mentorData)
        val userId = request.pathVariable("id").toLong()
        return ApiMessage(
            message = i18n("label.common.updated"),
            service.updateUserMentorInfo(userId, mentorData)
        ).toOkBody()
    }

    override fun delete(request: ServerRequest): ServerResponse {
        request.assertHasOneOfPermission(ScopePermission.MENTORS)
        val userId = request.pathVariable("id").toLong()
        service.deleteUserMentor(userId)
        return ApiMessage(data = null, message = i18n("label.common.deleted")).toOkBody()
    }
}
