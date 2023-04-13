package ru.mephi.alumniclub.app.controller.handler

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse

@Tag(name = "FCM tokens API")
interface FcmTokenHandler {
    @Operation(description = "Upload FCM token")
    fun uploadToken(request: ServerRequest): ServerResponse

    @Operation(description = "Remove FCM token")
    fun removeToken(request: ServerRequest): ServerResponse
}