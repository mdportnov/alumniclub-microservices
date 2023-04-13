package ru.mephi.alumniclub.app.controller.handler

import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse

interface PreferenceHandler {
    fun getPreferences(request: ServerRequest): ServerResponse
    fun updatePreferences(request: ServerRequest): ServerResponse
    fun getPreferencesOfUser(request: ServerRequest): ServerResponse
    fun turnOffEmailPreference(request: ServerRequest): ServerResponse
}