package ru.mephi.alumniclub.shared.dto.common

class ValidationError(
    val field: String,
    val rejectedValue: Any?,
    val description: String? = null
)
