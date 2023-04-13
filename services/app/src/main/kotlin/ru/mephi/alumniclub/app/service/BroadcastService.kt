package ru.mephi.alumniclub.app.service

import ru.mephi.alumniclub.app.database.entity.broadcast.Broadcast
import ru.mephi.alumniclub.app.model.dto.broadcast.request.BroadcastNotificationRequest
import ru.mephi.alumniclub.app.model.dto.broadcast.request.BroadcastRequest
import ru.mephi.alumniclub.app.model.dto.broadcast.response.BroadcastNotificationResponse
import ru.mephi.alumniclub.app.model.dto.broadcast.response.BroadcastNotificationShortResponse
import java.util.*
import javax.servlet.http.Part

interface BroadcastService {
    fun findById(id: UUID): Broadcast
    fun findEntityOrNullById(id: UUID): Broadcast?
    fun getById(id: UUID): BroadcastNotificationResponse
    fun getShortById(id: UUID): BroadcastNotificationShortResponse
    fun create(userId: Long, request: BroadcastRequest): BroadcastNotificationResponse
    fun uploadPhoto(id: UUID, file: Part): BroadcastNotificationResponse
    fun update(id: UUID, request: BroadcastNotificationRequest): BroadcastNotificationResponse
    fun delete(id: UUID)
}
