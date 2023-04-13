package ru.mephi.alumniclub.broadcast.service.impl

import org.springframework.stereotype.Service
import ru.mephi.alumniclub.broadcast.service.EmailSender
import ru.mephi.alumniclub.broadcast.service.EmailService
import ru.mephi.alumniclub.shared.dto.mail.BroadcastMail
import ru.mephi.alumniclub.shared.dto.mail.PublicationContentMail
import ru.mephi.alumniclub.shared.dto.mail.ResetPasswordMail
import ru.mephi.alumniclub.shared.dto.mail.VerifyEmailMail

@Service
class EmailServiceImpl(
    private val sender: EmailSender
) : EmailService {

    /**
     * Uses [PublicationContentMail] for sending content broadcast mail
     */
    override fun sendContentBroadcastMail(mail: PublicationContentMail) {
        mail.receivers.forEach {
            it.personalData["unsubscribeLink"] = mail.unsubscribeLink + "/" + it.personalData["unsubscribeEmailToken"]
        }
        sender.sendEmail(mail)
    }

    /**
     * Uses [ResetPasswordMail] for sending mail for reset password
     */
    override fun sendResetPasswordMail(mail: ResetPasswordMail) {
        sender.sendEmail(mail)
    }

    /**
     * Uses [VerifyEmailMail] for sending mail for verify user email
     */
    override fun sendVerifyEmailMail(mail: VerifyEmailMail) {
        sender.sendEmail(mail)
    }

    /**
     * Uses [BroadcastMail] for sending personal broadcast mail
     */
    override fun sendBroadcastMail(mail: BroadcastMail) {
        if (!mail.ignorePreferences) mail.receivers.forEach {
            it.personalData["unsubscribeLink"] = mail.unsubscribeLink + "/" + it.personalData["unsubscribeEmailToken"]
        }
        sender.sendEmail(mail)
    }
}