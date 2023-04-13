package ru.mephi.alumniclub.app.controller.handler

import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse

interface ExcelHandler {
    fun exportUsers(request: ServerRequest): ServerResponse
    fun exportEventParticipants(request: ServerRequest): ServerResponse
}
