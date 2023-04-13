package ru.mephi.alumniclub.app.controller.handler

import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse

interface NotificationHandler {
    fun listForUser(request: ServerRequest): ServerResponse
    fun listForAdmin(request: ServerRequest): ServerResponse
    fun findByIdForUser(request: ServerRequest): ServerResponse
    fun findByIdForAdmin(request: ServerRequest): ServerResponse
    fun getUserNotificationsInfo(request: ServerRequest): ServerResponse
    fun deleteForUser(request: ServerRequest): ServerResponse
    fun delete(request: ServerRequest): ServerResponse
}
