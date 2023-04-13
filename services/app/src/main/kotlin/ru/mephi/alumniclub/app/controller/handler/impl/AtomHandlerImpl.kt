package ru.mephi.alumniclub.app.controller.handler.impl

import org.springframework.stereotype.Component
import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse
import org.springframework.web.servlet.function.ServerResponse.created
import org.springframework.web.servlet.function.ServerResponse.ok
import org.springframework.web.servlet.function.body
import ru.mephi.alumniclub.app.controller.handler.AtomHandler
import ru.mephi.alumniclub.app.model.dto.atom.request.SendAtomsRequest
import ru.mephi.alumniclub.app.service.AtomService
import ru.mephi.alumniclub.shared.dto.common.ApiMessage
import ru.mephi.alumniclub.shared.dto.security.ScopePermission
import ru.mephi.alumniclub.shared.util.constants.API_VERSION_1
import ru.mephi.alumniclub.shared.util.constants.SERVER_NAME
import ru.mephi.alumniclub.shared.util.extension.assertHasOneOfPermission
import ru.mephi.alumniclub.shared.util.extension.getPrincipal
import ru.mephi.alumniclub.shared.util.extension.paramOrThrow
import ru.mephi.alumniclub.shared.util.extension.toOkBody
import ru.mephi.alumniclub.shared.util.response.ResponseManager
import java.net.URI
import java.util.*

@Component
class AtomHandlerImpl(
    private val service: AtomService
) : ResponseManager(), AtomHandler {

    override fun getUserAtomHistory(request: ServerRequest): ServerResponse {
        request.assertHasOneOfPermission(ScopePermission.ATOMS)
        val userId = request.paramOrThrow<Long>("userId")
        return ok().body(service.getUserAtomHistory(userId))
    }

    override fun getSelfAtomHistory(request: ServerRequest): ServerResponse {
        val userId = request.getPrincipal()
        return ok().body(service.getUserAtomHistory(userId))
    }

    override fun accrueAtoms(request: ServerRequest): ServerResponse {
        request.assertHasOneOfPermission(ScopePermission.ATOMS)
        val newAtoms = request.body<SendAtomsRequest>()
        validate(newAtoms)
        val adminId = request.getPrincipal()
        val message = i18n(
            if (newAtoms.sign) "label.atom.accrual" else "label.atom.withdraw",
            newAtoms.amount.toString()
        )
        val response = ApiMessage(message, data = service.accrueAtoms(adminId, newAtoms))
        return created(URI("$SERVER_NAME$API_VERSION_1/admin/atom?userId=${newAtoms.userId}")).body(response)
    }

    override fun deleteAtomHistoryElement(request: ServerRequest): ServerResponse {
        request.assertHasOneOfPermission(ScopePermission.ATOMS)
        val userId = request.paramOrThrow<Long>("userId")
        val atomId = request.paramOrThrow<UUID>("atomId")
        service.deleteAtomHistoryById(userId, atomId)
        return ApiMessage(data = null, message = i18n("label.common.deleted")).toOkBody()
    }
}
