package ru.mephi.alumniclub.app.controller.handler.impl

import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse
import org.springframework.web.servlet.function.body
import ru.mephi.alumniclub.app.controller.handler.CarouselHandler
import ru.mephi.alumniclub.app.model.dto.news.CarouselRequest
import ru.mephi.alumniclub.app.service.CarouselService
import ru.mephi.alumniclub.shared.dto.common.ApiError
import ru.mephi.alumniclub.shared.dto.common.ApiMessage
import ru.mephi.alumniclub.shared.dto.security.ScopePermission
import ru.mephi.alumniclub.shared.util.extension.assertHasOneOfPermission
import ru.mephi.alumniclub.shared.util.extension.getMultiPartPhoto
import ru.mephi.alumniclub.shared.util.extension.pathVariableOrThrow
import ru.mephi.alumniclub.shared.util.extension.toOkBody
import ru.mephi.alumniclub.shared.util.response.ResponseManager

@Service
class CarouselHandlerImpl(
    private val service: CarouselService
) : ResponseManager(), CarouselHandler {

    override fun create(request: ServerRequest): ServerResponse {
        request.assertHasOneOfPermission(ScopePermission.CAROUSEL)
        val carouselRequest = request.body<CarouselRequest>()
        validate(request)
        val response = service.create(carouselRequest)
        return ApiMessage(message = i18n("label.common.created"), response).toOkBody()
    }

    override fun update(request: ServerRequest): ServerResponse {
        request.assertHasOneOfPermission(ScopePermission.CAROUSEL)
        val carouselRequest = request.body<CarouselRequest>()
        validate(request)
        val newsId = request.pathVariableOrThrow<Long>("id")
        val response = service.update(newsId, carouselRequest)
        return ApiMessage(message = i18n("label.common.updated"), response).toOkBody()
    }

    override fun remove(request: ServerRequest): ServerResponse {
        request.assertHasOneOfPermission(ScopePermission.CAROUSEL)
        val newsId = request.pathVariableOrThrow<Long>("id")
        service.deleteById(newsId)
        return ApiMessage(data = null, message = i18n("label.common.deleted")).toOkBody()
    }

    override fun getAll(request: ServerRequest): ServerResponse {
        val response = service.getAll()
        return response.toOkBody()
    }

    override fun getById(request: ServerRequest): ServerResponse {
        val id = request.pathVariableOrThrow<Long>("id")
        val response = service.getById(id)
        return response.toOkBody()
    }

    override fun uploadPhoto(request: ServerRequest): ServerResponse {
        request.assertHasOneOfPermission(ScopePermission.CAROUSEL)
        val id = request.pathVariableOrThrow<Long>("id")
        val file =
            request.getMultiPartPhoto()
                ?: throw ApiError(HttpStatus.BAD_REQUEST, i18n("exception.media.invalidMultipart"))
        val response = service.uploadPhoto(id, file)
        return ApiMessage(message = i18n("label.common.updated"), response).toOkBody()
    }

}
