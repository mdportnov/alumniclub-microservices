package ru.mephi.alumniclub.app.database.repository.user

import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository
import ru.mephi.alumniclub.app.database.entity.user.VerifyEmailToken
import ru.mephi.alumniclub.shared.database.repository.AbstractRepository
import java.util.*

@Repository
interface VerifyEmailTokenDao : AbstractRepository<VerifyEmailToken>,
    PagingAndSortingRepository<VerifyEmailToken, Long> {
    fun findByToken(token: String): Optional<VerifyEmailToken>
}
