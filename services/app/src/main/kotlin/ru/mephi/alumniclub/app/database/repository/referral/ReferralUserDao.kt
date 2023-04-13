package ru.mephi.alumniclub.app.database.repository.referral

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import ru.mephi.alumniclub.app.database.entity.referral.ReferralUser
import ru.mephi.alumniclub.shared.database.repository.AbstractRepository
import java.util.*

@Repository
interface ReferralUserDao : CrudRepository<ReferralUser, Long>, AbstractRepository<ReferralUser> {
    @Query("SELECT ru FROM ReferralUser ru WHERE ru.referral.id = :userId")
    fun findEntityByReferralId(userId: Long): Optional<ReferralUser>
}
