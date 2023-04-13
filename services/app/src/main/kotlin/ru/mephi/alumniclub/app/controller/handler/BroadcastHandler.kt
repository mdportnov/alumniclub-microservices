package ru.mephi.alumniclub.app.controller.handler

import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse

interface BroadcastHandler {
    fun createBroadcast(request: ServerRequest): ServerResponse
    fun delete(request: ServerRequest): ServerResponse
    fun update(request: ServerRequest): ServerResponse
    fun uploadPhoto(request: ServerRequest): ServerResponse
    fun getShortById(request: ServerRequest): ServerResponse
    fun getById(request: ServerRequest): ServerResponse
}