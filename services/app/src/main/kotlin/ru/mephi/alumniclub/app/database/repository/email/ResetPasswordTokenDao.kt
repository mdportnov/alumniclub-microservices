package ru.mephi.alumniclub.app.database.repository.email

import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository
import ru.mephi.alumniclub.app.database.entity.email.ResetPasswordToken
import ru.mephi.alumniclub.shared.database.repository.AbstractRepository
import java.util.*

@Repository
interface ResetPasswordTokenDao : PagingAndSortingRepository<ResetPasswordToken, Long>,
    AbstractRepository<ResetPasswordToken> {

    fun findByUserId(userId: Long): Optional<ResetPasswordToken>
    fun findByToken(token: String): Optional<ResetPasswordToken>
}
