package ru.mephi.alumniclub.broadcast.service

import ru.mephi.alumniclub.shared.dto.mail.AbstractMail

interface EmailSender {
    fun sendEmail(metadata: AbstractMail)
}