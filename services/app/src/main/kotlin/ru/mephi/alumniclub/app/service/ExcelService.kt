package ru.mephi.alumniclub.app.service

import org.springframework.core.io.ByteArrayResource
import java.util.*

interface ExcelService {
    fun exportUsers(): ByteArrayResource
    fun exportEventParticipants(eventId: UUID): ByteArrayResource
}
