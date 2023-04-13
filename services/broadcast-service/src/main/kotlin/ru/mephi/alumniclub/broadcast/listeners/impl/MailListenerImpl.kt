package ru.mephi.alumniclub.broadcast.listeners.impl

import org.springframework.amqp.rabbit.annotation.EnableRabbit
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Component
import ru.mephi.alumniclub.broadcast.listeners.MailListener
import ru.mephi.alumniclub.broadcast.service.EmailService
import ru.mephi.alumniclub.shared.logging.AlumniLogger
import ru.mephi.alumniclub.shared.dto.mail.BroadcastMail
import ru.mephi.alumniclub.shared.dto.mail.PublicationContentMail
import ru.mephi.alumniclub.shared.dto.mail.ResetPasswordMail
import ru.mephi.alumniclub.shared.dto.mail.VerifyEmailMail
import ru.mephi.alumniclub.shared.util.constants.MAIL_BROADCAST
import ru.mephi.alumniclub.shared.util.constants.MAIL_PUBLICATION_CONTENT
import ru.mephi.alumniclub.shared.util.constants.MAIL_RESET_PASSWORD
import ru.mephi.alumniclub.shared.util.constants.MAIL_VERIFY_EMAIL

@Component
@EnableRabbit
class MailListenerImpl(
    private val emailService: EmailService,
    private val logger: AlumniLogger
) : MailListener {

    @RabbitListener(queues = [MAIL_PUBLICATION_CONTENT])
    override fun contentBroadcastMail(mail: PublicationContentMail) {
        try {
            emailService.sendContentBroadcastMail(mail)
        } catch (e: Exception) {
            logger.error("ERROR SEND CONTENT BROADCAST MAIL", e)
        }
    }

    @RabbitListener(queues = [MAIL_RESET_PASSWORD])
    override fun resetPasswordEmail(mail: ResetPasswordMail) {
        try {
            emailService.sendResetPasswordMail(mail)
        } catch (e: Exception) {
            logger.error("ERROR SEND RESET PASSWORD MAIL", e)
        }
    }

    @RabbitListener(queues = [MAIL_VERIFY_EMAIL])
    override fun verifyEmailMail(mail: VerifyEmailMail) {
        try {
            emailService.sendVerifyEmailMail(mail)
        } catch (e: Exception) {
            logger.error("ERROR SEND VERIFY EMAIL MAIL", e)
        }
    }

    @RabbitListener(queues = [MAIL_BROADCAST])
    override fun broadcastMail(mail: BroadcastMail) {
        try {
            emailService.sendBroadcastMail(mail)
        } catch (e: Exception) {
            logger.error("ERROR SEND PERSONAL BROADCAST MAIL", e)
        }
    }
}