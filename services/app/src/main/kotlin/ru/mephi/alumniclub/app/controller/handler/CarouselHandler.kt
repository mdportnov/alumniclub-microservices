package ru.mephi.alumniclub.app.controller.handler

import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse

interface CarouselHandler {
    fun create(request: ServerRequest): ServerResponse
    fun update(request: ServerRequest): ServerResponse
    fun remove(request: ServerRequest): ServerResponse
    fun getAll(request: ServerRequest): ServerResponse
    fun getById(request: ServerRequest): ServerResponse
    fun uploadPhoto(request: ServerRequest): ServerResponse
}