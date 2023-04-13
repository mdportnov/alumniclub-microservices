package ru.mephi.alumniclub.app.controller.handler

import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse

interface UserHandler {
    fun listForAdmin(request: ServerRequest): ServerResponse
    fun findByIdForAdmin(request: ServerRequest): ServerResponse
    fun findProfileByIdForAdmin(request: ServerRequest): ServerResponse
    fun findBioByIdForAdmin(request: ServerRequest): ServerResponse
    fun findUserByIdForUser(request: ServerRequest): ServerResponse
    fun previewSelf(request: ServerRequest): ServerResponse
    fun getUserDevices(request: ServerRequest): ServerResponse
    fun getSelfDevices(request: ServerRequest): ServerResponse
    fun getSelf(request: ServerRequest): ServerResponse
    fun getSelfBio(request: ServerRequest): ServerResponse
    fun refreshPassword(request: ServerRequest): ServerResponse
    fun updateProfile(request: ServerRequest): ServerResponse
    fun updateBio(request: ServerRequest): ServerResponse
    fun uploadPhoto(request: ServerRequest): ServerResponse
    fun deleteSelf(request: ServerRequest): ServerResponse
    fun getVisibility(request: ServerRequest): ServerResponse
    fun updateVisibility(request: ServerRequest): ServerResponse
    fun listUserCommunities(request: ServerRequest): ServerResponse
    fun setBanStatus(request: ServerRequest): ServerResponse
    fun delete(request: ServerRequest): ServerResponse
}
