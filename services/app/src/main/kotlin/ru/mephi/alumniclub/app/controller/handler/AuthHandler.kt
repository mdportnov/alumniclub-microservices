package ru.mephi.alumniclub.app.controller.handler

import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse

interface AuthHandler {
    fun register(request: ServerRequest): ServerResponse
    fun login(request: ServerRequest): ServerResponse
    fun refresh(request: ServerRequest): ServerResponse
    fun logout(request: ServerRequest): ServerResponse
    fun resetPassword(request: ServerRequest): ServerResponse
    fun resetRefreshPassword(request: ServerRequest): ServerResponse
    fun checkResetPasswordTokenExists(request: ServerRequest): ServerResponse
    fun refreshPassword(request: ServerRequest): ServerResponse
    fun verifyEmail(request: ServerRequest): ServerResponse
}
