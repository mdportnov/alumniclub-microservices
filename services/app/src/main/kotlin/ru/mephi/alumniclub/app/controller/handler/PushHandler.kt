package ru.mephi.alumniclub.app.controller.handler

import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse

interface PushHandler {
    fun sendPushNotification(request: ServerRequest): ServerResponse
}