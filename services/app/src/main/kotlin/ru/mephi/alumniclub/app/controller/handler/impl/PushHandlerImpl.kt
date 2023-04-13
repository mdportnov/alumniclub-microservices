package ru.mephi.alumniclub.app.controller.handler.impl

import org.springframework.stereotype.Service
import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse
import org.springframework.web.servlet.function.body
import ru.mephi.alumniclub.app.controller.handler.PushHandler
import ru.mephi.alumniclub.app.model.dto.broadcast.request.BroadcastRequest
import ru.mephi.alumniclub.app.service.PushService
import ru.mephi.alumniclub.shared.dto.common.ApiMessage
import ru.mephi.alumniclub.shared.model.enums.Authority
import ru.mephi.alumniclub.shared.util.extension.assertHasAuthority
import ru.mephi.alumniclub.shared.util.extension.toOkBody
import ru.mephi.alumniclub.shared.util.response.ResponseManager

@Service
class PushHandlerImpl(
    private val pushService: PushService,
) : ResponseManager(), PushHandler {

    override fun sendPushNotification(request: ServerRequest): ServerResponse {
        request.assertHasAuthority(Authority.ADMIN)
        val push = request.body<BroadcastRequest>()
        pushService.sendSimpleTextNotification(push)
        return ApiMessage(data = null, message = i18n("label.push.send")).toOkBody()
    }
}
