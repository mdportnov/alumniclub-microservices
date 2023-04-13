package ru.mephi.alumniclub.app.controller.handler

import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse

interface SurveyHandler {
    fun create(request: ServerRequest): ServerResponse
    fun update(request: ServerRequest): ServerResponse
    fun uploadPhoto(request: ServerRequest): ServerResponse
    fun vote(request: ServerRequest): ServerResponse
    fun pageList(request: ServerRequest): ServerResponse
    fun cursorList(request: ServerRequest): ServerResponse
    fun getById(request: ServerRequest): ServerResponse
    fun getSelfAnswer(request: ServerRequest): ServerResponse
    fun getAnswerOfUser(request: ServerRequest): ServerResponse
    fun getUsersByVariant(request: ServerRequest): ServerResponse
    fun getAnswersList(request: ServerRequest): ServerResponse
    fun getSurveyAnswersInfo(request: ServerRequest): ServerResponse
    fun getSurveyMetadata(request: ServerRequest): ServerResponse
    fun delete(request: ServerRequest): ServerResponse
}