package ru.mephi.alumniclub.shared.dto.mail

import ru.mephi.alumniclub.shared.util.enums.RabbitMessageType

class VerifyEmailMail(
    val link: String,
    val tokenTtl: Long,
    val email: String
) : AbstractMail(
    subject = "Подтверждение почты",
    context = mapOf(
        "link" to link,
        "tokenTtl" to tokenTtl
    ),
    receivers = listOf(MailReceiver(email))
) {
    override val type = RabbitMessageType.MAIL_VERIFY_EMAIL
    override fun templateLocation() = "mails/VerifyEmailMail.html"
}