package ru.mephi.alumniclub.app.model.mapper.broadcast

import org.springframework.stereotype.Component
import ru.mephi.alumniclub.app.database.entity.broadcast.Broadcast
import ru.mephi.alumniclub.app.model.dto.broadcast.request.BroadcastNotificationRequest
import ru.mephi.alumniclub.app.model.dto.broadcast.request.BroadcastRequest
import ru.mephi.alumniclub.app.model.dto.broadcast.response.BroadcastNotificationResponse
import ru.mephi.alumniclub.app.model.dto.broadcast.response.BroadcastNotificationShortResponse
import ru.mephi.alumniclub.app.model.mapper.user.UserMapper
import ru.mephi.alumniclub.app.service.UserService

@Component
class BroadcastNotificationMapper(
    private val userService: UserService,
    private val userMapper: UserMapper
) {
    fun asEntity(request: BroadcastRequest, userId: Long): Broadcast {
        val broadcast = Broadcast(
            title = request.title,
            content = request.content
        )
        broadcast.author = userService.findUserEntityById(userId)
        return broadcast
    }

    fun asResponse(entity: Broadcast): BroadcastNotificationResponse {
        return BroadcastNotificationResponse(
            id = entity.id,
            createdAt = entity.createdAt,
            title = entity.title,
            content = entity.content,
            photoPath = entity.photoPath,
            author = entity.author?.let { userMapper.asNullableShortResponse(it) },
        )
    }

    fun asShortResponse(entity: Broadcast): BroadcastNotificationShortResponse {
        return BroadcastNotificationShortResponse(
            id = entity.id,
            title = entity.title,
            content = entity.content,
            photoPath = entity.photoPath,
            author = entity.author?.let { userMapper.asNullableShortResponse(it) },
        )
    }

    fun update(entity: Broadcast, request: BroadcastNotificationRequest) {
        entity.title = request.title
        entity.content = request.content
    }
}
