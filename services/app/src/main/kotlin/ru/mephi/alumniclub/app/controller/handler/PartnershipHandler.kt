package ru.mephi.alumniclub.app.controller.handler

import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse

interface PartnershipHandler {
    fun listForAdmin(request: ServerRequest): ServerResponse
    fun listForPublic(request: ServerRequest): ServerResponse
    fun create(request: ServerRequest): ServerResponse
    fun findById(request: ServerRequest): ServerResponse
    fun update(request: ServerRequest): ServerResponse
    fun uploadPhoto(request: ServerRequest): ServerResponse
    fun delete(request: ServerRequest): ServerResponse
    fun listMembers(request: ServerRequest): ServerResponse
}