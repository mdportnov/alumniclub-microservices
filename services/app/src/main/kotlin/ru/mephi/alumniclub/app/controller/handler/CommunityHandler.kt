package ru.mephi.alumniclub.app.controller.handler

import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse

interface CommunityHandler {
    fun list(request: ServerRequest): ServerResponse
    fun listSelf(request: ServerRequest): ServerResponse
    fun listNotSelf(request: ServerRequest): ServerResponse
    fun findById(request: ServerRequest): ServerResponse
    fun create(request: ServerRequest): ServerResponse
    fun update(request: ServerRequest): ServerResponse
    fun uploadPhoto(request: ServerRequest): ServerResponse
    fun delete(request: ServerRequest): ServerResponse
    fun listMembersForUser(request: ServerRequest): ServerResponse
    fun listMembersForAdmin(request: ServerRequest): ServerResponse
    fun participate(request: ServerRequest): ServerResponse
    fun kick(request: ServerRequest): ServerResponse
}
