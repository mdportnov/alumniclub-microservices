package ru.mephi.alumniclub.app.controller.handler

import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse

interface RegistrationLimiterHandler {
    fun setSettings(request: ServerRequest): ServerResponse
    fun getSettings(request: ServerRequest): ServerResponse
}