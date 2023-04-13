package ru.mephi.alumniclub.app.database.repository.referral

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import ru.mephi.alumniclub.app.database.entity.referral.Affiliate
import ru.mephi.alumniclub.app.database.entity.user.User
import ru.mephi.alumniclub.shared.database.repository.AbstractRepository
import java.util.*

@Repository
interface AffiliateDao : AbstractRepository<Affiliate> {
    fun findByToken(token: String): Optional<Affiliate>

    @Query("SELECT ru.referral FROM ReferralUser ru WHERE ru.affiliate.id = :userId")
    fun findAllReferralsByUserId(@Param("userId") userId: Long): List<User>

    @Query("SELECT ru.affiliate FROM ReferralUser ru WHERE ru.referral.id = :userId")
    fun findAffiliateByReferralId(userId: Long): Optional<Affiliate>

    @Query("SELECT af FROM Affiliate af ORDER BY af.referrals.size DESC")
    fun findTopByReferralCount(pageable: Pageable): Page<Affiliate>

    @Query(
        """SELECT af FROM Affiliate af
        WHERE CONCAT(af.user.name, ' ', af.user.surname, ' ', af.user.patronymic) 
        LIKE CONCAT('%', :string, '%')
        ORDER BY af.user.name DESC
    """
    )
    fun findAffiliatesByContains(@Param("string") string: String): List<Affiliate>

}
