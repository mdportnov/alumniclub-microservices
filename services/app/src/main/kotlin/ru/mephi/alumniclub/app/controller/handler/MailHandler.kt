package ru.mephi.alumniclub.app.controller.handler

import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse

interface MailHandler {
    fun sendTextEmail(request: ServerRequest): ServerResponse
}