package ru.mephi.alumniclub.app.controller.handler

import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse

interface VersionHandler {
    fun isUpdateRequired(request: ServerRequest): ServerResponse
    fun getMinSupportedAPIVersion(request: ServerRequest): ServerResponse
    fun getBackendVersion(request: ServerRequest): ServerResponse
}