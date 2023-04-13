package ru.mephi.alumniclub.app.controller.handler

import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse

@Tag(name = "Static files API")
interface StaticHandler {
    fun createContentPhoto(request: ServerRequest): ServerResponse
    fun getUploadedPhoto(request: ServerRequest): ServerResponse
    fun getPublicationPhotoById(request: ServerRequest): ServerResponse
}
