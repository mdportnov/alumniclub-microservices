package ru.mephi.alumniclub.app.controller.handler

import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse

interface MerchHandler {
    fun getAllAvailableMerch(request: ServerRequest): ServerResponse
    fun getAllMerch(request: ServerRequest): ServerResponse
    fun getMerchById(request: ServerRequest): ServerResponse
    fun create(request: ServerRequest): ServerResponse
    fun uploadPhoto(request: ServerRequest): ServerResponse
    fun updateMerch(request: ServerRequest): ServerResponse
    fun deleteMerch(request: ServerRequest): ServerResponse
}