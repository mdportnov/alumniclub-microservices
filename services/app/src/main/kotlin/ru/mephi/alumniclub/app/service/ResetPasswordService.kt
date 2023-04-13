package ru.mephi.alumniclub.app.service

import ru.mephi.alumniclub.app.database.entity.email.ResetPasswordToken
import ru.mephi.alumniclub.app.database.entity.user.User

interface ResetPasswordService {
    fun createNewToken(user: User): ResetPasswordToken
    fun getLinkToResetPassword(token: ResetPasswordToken): String
    fun tokenIsExist(token: String): Boolean
}