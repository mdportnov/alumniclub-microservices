package ru.mephi.alumniclub.shared.dto

import java.time.LocalDateTime

abstract class AbstractCreatedAtResponse<T>(
    override var id: T,
    open var createdAt: LocalDateTime
) : AbstractResponse<T>(id)
