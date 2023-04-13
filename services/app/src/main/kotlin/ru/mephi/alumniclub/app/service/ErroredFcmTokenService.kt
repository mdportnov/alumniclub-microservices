package ru.mephi.alumniclub.app.service

import ru.mephi.alumniclub.app.database.entity.fcm.ErroredFcmToken

interface ErroredFcmTokenService {
    fun saveAll(list: List<ErroredFcmToken>): MutableIterable<ErroredFcmToken>
    fun findAll(): Iterable<ErroredFcmToken>
    fun delete(token: ErroredFcmToken)
}