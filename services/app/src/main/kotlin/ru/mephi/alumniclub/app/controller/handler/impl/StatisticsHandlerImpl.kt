package ru.mephi.alumniclub.app.controller.handler.impl

import org.springframework.stereotype.Service
import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse
import ru.mephi.alumniclub.app.controller.handler.StatisticsHandler
import ru.mephi.alumniclub.app.service.StatisticsService
import ru.mephi.alumniclub.shared.dto.security.ScopePermission
import ru.mephi.alumniclub.shared.util.extension.assertHasOneOfPermission
import ru.mephi.alumniclub.shared.util.extension.toOkBody

@Service
class StatisticsHandlerImpl(
    private val service: StatisticsService
) : StatisticsHandler {
    override fun getStatistics(request: ServerRequest): ServerResponse {
        request.assertHasOneOfPermission(ScopePermission.USERS)
        return service.getStatistics().toOkBody()
    }
}
