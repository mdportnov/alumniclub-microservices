package ru.mephi.alumniclub.shared.dto.mail

import ru.mephi.alumniclub.shared.util.enums.RabbitMessageType
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class PublicationContentMail(
    receivers: List<MailReceiver>,
    val title: String,
    val content: String,
    val date: LocalDateTime,
    val unsubscribeLink: String,
    val photoLink: String,
    val publicationLink: String,
    val baseUrl: String,
) : AbstractMail(
    subject = title,
    context = mapOf(
        "photoLink" to photoLink,
        "publicationLink" to publicationLink,
        "title" to title,
        "content" to content,
        "date" to DateTimeFormatter.ofPattern("dd-MM-yyyy").format(date),
        "baseUrl" to baseUrl
    ),
    receivers = receivers
) {
    override val type = RabbitMessageType.MAIL_PUBLICATION_CONTENT
    override fun templateLocation(): String = "mails/inline-news.html"
    override fun toString(): String {
        return "PublicationContentMail(photoLink='$photoLink', publicationLink='$publicationLink', title='$title', content='$content', date=$date, unsubscribeLink='$unsubscribeLink', baseUrl='$baseUrl') ${super.toString()}"
    }
}