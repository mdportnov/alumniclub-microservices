package ru.mephi.alumniclub.app.controller.handler

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse

@Tag(name = "Mentor API")
interface MentorHandler {
    @Operation(description = "List Mentors")
    fun listAll(request: ServerRequest): ServerResponse

    @Operation(description = "Get Mentor by id")
    fun getByIdForPublic(request: ServerRequest): ServerResponse
    fun getByIdForAdmin(request: ServerRequest): ServerResponse

    @Operation(description = "Create new Mentor")
    fun create(request: ServerRequest): ServerResponse

    @Operation(description = "Delete Mentor Data")
    fun delete(request: ServerRequest): ServerResponse
    fun toggleMentorAvailability(request: ServerRequest): ServerResponse
    fun update(request: ServerRequest): ServerResponse
    fun listAllAvailable(request: ServerRequest): ServerResponse
}
