package ru.mephi.alumniclub.app.controller.handler

import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse

interface AtomHandler {
    fun getSelfAtomHistory(request: ServerRequest): ServerResponse
    fun getUserAtomHistory(request: ServerRequest): ServerResponse
    fun accrueAtoms(request: ServerRequest): ServerResponse
    fun deleteAtomHistoryElement(request: ServerRequest): ServerResponse
}
