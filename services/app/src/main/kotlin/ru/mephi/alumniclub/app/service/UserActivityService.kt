package ru.mephi.alumniclub.app.service

interface UserActivityService {
    fun handle(principal: Long)
}