package ru.mephi.alumniclub.shared.util

import org.springframework.data.domain.PageRequest
import java.time.LocalDateTime

class Cursor(
    val from: LocalDateTime,
    val page: PageRequest,
    var chronology: Chronology
) {
    enum class Chronology { BEFORE, AFTER }
}
