package ru.mephi.alumniclub.broadcast.service

import ru.mephi.alumniclub.shared.dto.mail.BroadcastMail
import ru.mephi.alumniclub.shared.dto.mail.PublicationContentMail
import ru.mephi.alumniclub.shared.dto.mail.ResetPasswordMail
import ru.mephi.alumniclub.shared.dto.mail.VerifyEmailMail

interface EmailService {
    fun sendContentBroadcastMail(mail: PublicationContentMail)
    fun sendResetPasswordMail(mail: ResetPasswordMail)
    fun sendVerifyEmailMail(mail: VerifyEmailMail)
    fun sendBroadcastMail(mail: BroadcastMail)
}