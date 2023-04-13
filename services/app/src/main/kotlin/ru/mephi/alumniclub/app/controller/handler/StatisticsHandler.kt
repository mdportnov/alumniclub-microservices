package ru.mephi.alumniclub.app.controller.handler

import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse

interface StatisticsHandler {
    fun getStatistics(request: ServerRequest): ServerResponse
}
