package ru.mephi.alumniclub.app.controller.handler

import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse

interface FeatureToggleHandler {
    fun changeFeatureState(request: ServerRequest): ServerResponse
    fun listAllFeatures(request: ServerRequest): ServerResponse
    fun getFeatureByName(request: ServerRequest): ServerResponse
}
