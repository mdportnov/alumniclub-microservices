package ru.mephi.alumniclub.shared.dto.mail

import ru.mephi.alumniclub.shared.util.enums.RabbitMessageType

class ResetPasswordMail(
    val email: String,
    val link: String
) : AbstractMail(
    subject = "Сброс пароля",
    receivers = listOf(MailReceiver(email, mutableMapOf("link" to link))),
) {
    override val type = RabbitMessageType.MAIL_RESET_PASSWORD
    override fun templateLocation(): String = "mails/ResetPasswordEmail.html"
}