package ru.mephi.alumniclub.app.service

import ru.mephi.alumniclub.app.database.entity.user.User
import ru.mephi.alumniclub.app.database.entity.user.VerifyEmailToken

interface VerifyEmailService {
    fun sendVerificationEmail(user: User)
    fun verifyEmail(token: String)
    fun emailIsVerified(user: User): Boolean
    fun emailVerificationIsExpired(id: Long): Boolean
    fun findAllUnVerified(): Iterable<VerifyEmailToken>
    fun tokenIsExpired(token: VerifyEmailToken): Boolean
}