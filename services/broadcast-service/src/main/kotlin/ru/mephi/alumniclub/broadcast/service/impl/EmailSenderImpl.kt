package ru.mephi.alumniclub.broadcast.service.impl

import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service
import org.thymeleaf.context.Context
import org.thymeleaf.spring5.SpringTemplateEngine
import ru.mephi.alumniclub.broadcast.config.properties.MailProperties
import ru.mephi.alumniclub.broadcast.service.EmailSender
import ru.mephi.alumniclub.shared.dto.mail.AbstractMail
import ru.mephi.alumniclub.shared.logging.AlumniLogger
import javax.mail.Message
import javax.mail.internet.InternetAddress

@Service
class EmailSenderImpl(
    private val templateEngine: SpringTemplateEngine,
    private val mailSender: JavaMailSender,
    private val properties: MailProperties,
    private val logger: AlumniLogger
) : EmailSender {

    /**
     * Sends mail to users
     *
     * @param metadata the data that will be used to create mails
     */

    override fun sendEmail(metadata: AbstractMail) {
        val mail = mailSender.createMimeMessage()
        val helper = MimeMessageHelper(mail, true)
        helper.setSubject(metadata.subject)
        helper.setFrom(properties.fromMail, properties.defaultFromName)
        metadata.receivers.forEach { receiver ->
            try {
                val address = InternetAddress.parse(receiver.email).first()

                val context = Context()
                context.setVariables(metadata.context + receiver.personalData)
                val emailBody = templateEngine.process(metadata.templateLocation(), context)
                helper.setText(emailBody, true)

                mail.setRecipient(Message.RecipientType.TO, address)

                logger.info(
                    """
                        Sending email ${metadata::class.simpleName} to $receiver:
                        ${emailMetadataToString(metadata)}
                    """.trimIndent()
                )

                mailSender.send(mail)
            } catch (e: Exception) {
                logger.error(
                    """
                    Error sending email ${metadata::class.simpleName} to $receiver:
                    ${emailMetadataToString(metadata)}
                    ${e.stackTraceToString()}
                    """.trimIndent()
                )
                return@forEach
            }
        }
    }

    private fun emailMetadataToString(metadata: AbstractMail): String {
        return """
            Subject: ${metadata.subject}
            Context: ${metadata.context}
            Receivers: ${metadata.receivers}
        """.trimIndent()
    }
}