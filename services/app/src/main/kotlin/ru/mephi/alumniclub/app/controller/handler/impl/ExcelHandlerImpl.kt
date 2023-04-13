package ru.mephi.alumniclub.app.controller.handler.impl

import org.springframework.core.io.ByteArrayResource
import org.springframework.http.ContentDisposition
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse
import org.springframework.web.servlet.function.ServerResponse.ok
import ru.mephi.alumniclub.app.controller.handler.ExcelHandler
import ru.mephi.alumniclub.app.service.ExcelService
import ru.mephi.alumniclub.app.service.PublicationService
import ru.mephi.alumniclub.shared.dto.security.ScopePermission
import ru.mephi.alumniclub.shared.util.extension.assertHasOneOfPermission
import ru.mephi.alumniclub.shared.util.extension.pathVariableOrThrow
import java.nio.charset.StandardCharsets
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


@Service
class ExcelHandlerImpl(
    private val excelService: ExcelService,
    private val publicationService: PublicationService
) : ExcelHandler {

    private fun ByteArrayResource.toServerResponse(filename: String): ServerResponse {
        val header = ContentDisposition
            .attachment()
            .filename(filename, StandardCharsets.UTF_8)
            .build().toString()
        return ok()
            .contentType(MediaType.APPLICATION_OCTET_STREAM)
            .contentLength(contentLength())
            .header(HttpHeaders.CONTENT_DISPOSITION, header)
            .body(this)
    }

    override fun exportUsers(request: ServerRequest): ServerResponse {
        request.assertHasOneOfPermission(ScopePermission.USERS)
        val table = excelService.exportUsers()
        val exportTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd-HH:mm"))
        val fileName = "Users_${exportTime}.xlsx"
        return table.toServerResponse(fileName)
    }

    override fun exportEventParticipants(request: ServerRequest): ServerResponse {
        request.assertHasOneOfPermission(ScopePermission.USERS)
        val eventId = request.pathVariableOrThrow<UUID>("eventId")
        val event = publicationService.findEventEntity(eventId, ignoreChecks = true)
        val table = excelService.exportEventParticipants(eventId)
        val exportTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd-HH:mm"))
        val fileName = "Participants_${event.title}_$exportTime.xlsx"
        return table.toServerResponse(fileName)
    }
}
