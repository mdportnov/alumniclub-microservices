package ru.mephi.alumniclub.shared.dto.common

import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDateTime

class ApiMessage<T>(
    override val message: String,
    val data: T? = null,
) : ApiCommonResponse {

    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss[.SSS]")
    val timestamp: LocalDateTime = LocalDateTime.now()
}


