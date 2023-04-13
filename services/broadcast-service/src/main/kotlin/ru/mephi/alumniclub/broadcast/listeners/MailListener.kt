package ru.mephi.alumniclub.broadcast.listeners

import ru.mephi.alumniclub.shared.dto.mail.BroadcastMail
import ru.mephi.alumniclub.shared.dto.mail.PublicationContentMail
import ru.mephi.alumniclub.shared.dto.mail.ResetPasswordMail
import ru.mephi.alumniclub.shared.dto.mail.VerifyEmailMail

interface MailListener {
    fun contentBroadcastMail(mail: PublicationContentMail)
    fun resetPasswordEmail(mail: ResetPasswordMail)
    fun verifyEmailMail(mail: VerifyEmailMail)
    fun broadcastMail(mail: BroadcastMail)
}