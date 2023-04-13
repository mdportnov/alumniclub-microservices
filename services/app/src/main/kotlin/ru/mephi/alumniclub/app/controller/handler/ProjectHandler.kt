package ru.mephi.alumniclub.app.controller.handler

import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse

interface ProjectHandler {
    fun listForAdmin(request: ServerRequest): ServerResponse
    fun listProjectsForPublic(request: ServerRequest): ServerResponse
    fun listForUser(request: ServerRequest): ServerResponse
    fun listByUser(request: ServerRequest): ServerResponse
    fun findProjectByIdForUser(request: ServerRequest): ServerResponse
    fun findProjectByIdForAdmin(request: ServerRequest): ServerResponse
    fun previewProjectById(request: ServerRequest): ServerResponse
    fun create(request: ServerRequest): ServerResponse
    fun update(request: ServerRequest): ServerResponse
    fun uploadPhoto(request: ServerRequest): ServerResponse
    fun archive(request: ServerRequest): ServerResponse
    fun delete(request: ServerRequest): ServerResponse
    fun listMembersForUser(request: ServerRequest): ServerResponse
    fun listMembersForAdmin(request: ServerRequest): ServerResponse
    fun participate(request: ServerRequest): ServerResponse
}
