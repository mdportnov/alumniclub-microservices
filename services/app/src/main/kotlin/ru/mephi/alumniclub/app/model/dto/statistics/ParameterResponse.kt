package ru.mephi.alumniclub.app.model.dto.statistics

class ParameterResponse(
    val name: String,
    val current: Long,
    prev: Long
) {
    val delta = if (current == 0L) 0 else (current - prev) / current * 100
}
