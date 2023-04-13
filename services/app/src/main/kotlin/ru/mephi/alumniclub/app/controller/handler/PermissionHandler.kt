package ru.mephi.alumniclub.app.controller.handler

import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse

interface PermissionHandler {
    fun list(request: ServerRequest): ServerResponse
    fun updateAdminRole(request: ServerRequest): ServerResponse
    fun findById(request: ServerRequest): ServerResponse
    fun findSelf(request: ServerRequest): ServerResponse
    fun updatePermissions(request: ServerRequest): ServerResponse
}
