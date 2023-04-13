package ru.mephi.alumniclub.shared.dto.mail

import ru.mephi.alumniclub.shared.util.enums.RabbitMessageType
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class BroadcastMail(
    receivers: List<MailReceiver>,
    val title: String,
    val content: String,
    val date: LocalDateTime,
    val baseUrl: String,
    val photoLink: String,
    var unsubscribeLink: String,
    val ignorePreferences: Boolean = false
) : AbstractMail(
    subject = title,
    context = mapOf(
        "photoLink" to photoLink,
        "title" to title,
        "content" to content,
        "date" to DateTimeFormatter.ofPattern("dd-MM-yyyy").format(date),
        "baseUrl" to baseUrl
    ),
    receivers = receivers
) {
    override val type = RabbitMessageType.MAIL_BROADCAST

    override fun templateLocation(): String {
        return if (ignorePreferences) "mails/broadcast-ignore-true.html"
        else "mails/broadcast-ignore-false.html"
    }
}