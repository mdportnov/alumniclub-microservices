package ru.mephi.alumniclub.app.controller.handler.impl

import org.springframework.http.ContentDisposition
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse
import org.springframework.web.servlet.function.ServerResponse.ok
import ru.mephi.alumniclub.app.controller.handler.StaticHandler
import ru.mephi.alumniclub.app.database.entity.publication.AbstractPublication
import ru.mephi.alumniclub.app.model.exception.common.ResourceNotFoundException
import ru.mephi.alumniclub.app.service.ContentPhotoService
import ru.mephi.alumniclub.app.service.FullPublicationService
import ru.mephi.alumniclub.app.service.StorageManager
import ru.mephi.alumniclub.shared.dto.common.ApiError
import ru.mephi.alumniclub.shared.util.extension.getMultiPartPhoto
import ru.mephi.alumniclub.shared.util.extension.pathVariableOrThrow
import ru.mephi.alumniclub.shared.util.extension.toOkBody
import ru.mephi.alumniclub.shared.util.response.ResponseManager
import java.io.File
import java.io.FileNotFoundException
import java.nio.charset.StandardCharsets
import java.util.*

@Component
class StaticHandlerImpl(
    private val storageManager: StorageManager,
    private val fullPublicationService: FullPublicationService,
    private val contentPhotoService: ContentPhotoService,
) : ResponseManager(), StaticHandler {
    override fun createContentPhoto(request: ServerRequest): ServerResponse {
        val file = request.getMultiPartPhoto()
            ?: throw ApiError(HttpStatus.BAD_REQUEST, i18n("exception.media.invalidMultipart"))
        val response = contentPhotoService.createContentPhoto(file)
        return response.toOkBody()
    }

    private fun getPhotoResponse(file: File): ServerResponse {
        val contentType = storageManager.checkAllowedExtension(file)
        return if (file.exists()) {
            val header = ContentDisposition
                .attachment()
                .filename(file.name, StandardCharsets.UTF_8)
                .build().toString()
            ok().contentType(contentType)
                .contentLength(file.length())
                .header(HttpHeaders.CONTENT_DISPOSITION, header)
                .body(file.readBytes())
        } else throw ResourceNotFoundException(File::class.java)
    }

    override fun getPublicationPhotoById(request: ServerRequest): ServerResponse {
        val id = request.pathVariableOrThrow<UUID>("id")
        val photoPath = fullPublicationService.getAbstractPublication(id).photoPath ?: throw ResourceNotFoundException(
            AbstractPublication::class.java, id
        )
        val file = storageManager.getPhotoFile(photoPath)
            ?: throw ApiError(
                HttpStatus.NOT_FOUND,
                i18n("exception.notFound.file", photoPath),
                exception = FileNotFoundException(photoPath)
            )
        return getPhotoResponse(file)
    }

    override fun getUploadedPhoto(request: ServerRequest): ServerResponse {
        val dir = request.pathVariable("dir")
        val fileName = request.pathVariable("fileName")
        val file = storageManager.getPhotoFile("$dir/$fileName")
            ?: throw ApiError(
                HttpStatus.NOT_FOUND,
                i18n("exception.notFound.file", fileName),
                exception = FileNotFoundException(fileName)
            )
        return getPhotoResponse(file)
    }
}
