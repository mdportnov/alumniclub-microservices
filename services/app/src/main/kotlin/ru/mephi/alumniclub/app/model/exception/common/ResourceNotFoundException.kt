package ru.mephi.alumniclub.app.model.exception.common

import org.springframework.http.HttpStatus
import ru.mephi.alumniclub.shared.dto.common.ApiError
import java.lang.reflect.Type
import java.util.*

class ResourceNotFoundException(
    override var message: String = "Ресурс не найден"
) : ApiError(
    message = message,
    status = HttpStatus.NOT_FOUND,
    debugMessage = "Resource not found",
) {
    constructor(type: Type) : this() {
        message = "${(type as Class<*>).simpleName} not found"
    }

    constructor(type: Type, id: Long) : this() {
        message = "${(type as Class<*>).simpleName} with id [$id] not found"
    }

    constructor(type: Type, id: UUID) : this() {
        message = "${(type as Class<*>).simpleName} with id [$id] not found"
    }

    constructor(type: Type, name: String) : this() {
        message = "${(type as Class<*>).simpleName} with name [$name] not found"
    }
}
