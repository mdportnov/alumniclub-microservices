package ru.mephi.alumniclub.app.database.repository.user


import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Lock
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.data.repository.query.Param
import ru.mephi.alumniclub.app.database.entity.user.User
import ru.mephi.alumniclub.app.model.dto.user.UserIdUserEmailPair
import ru.mephi.alumniclub.shared.database.repository.AbstractRepository
import java.time.LocalDateTime
import java.util.*
import javax.persistence.LockModeType


interface UserDao : AbstractRepository<User>, PagingAndSortingRepository<User, Long> {
    fun countByCreatedAtAfterAndCreatedAtBefore(after: LocalDateTime, before: LocalDateTime): Long
    fun findByModeratorTrueOrAdminTrueAndSurnameStartsWith(prefix: String, pageable: Pageable): Page<User>
    fun existsByEmail(email: String): Boolean
    fun findByEmail(email: String): Optional<User>

    fun findBySurnameStartsWith(prefix: String, pageable: Pageable): Page<User>

    fun deleteUserById(id: Long)

    @Query("SELECT id FROM User")
    fun findAllUsersIds(): List<Long>

    @Query("SELECT u.id AS userId, u.email as email FROM User u WHERE u.id IN :ids")
    fun findUserIdUserEmailPairs(ids: List<Long>): List<UserIdUserEmailPair>

    @Query(
        value = """
        SELECT DISTINCT u.id
        FROM User u
        WHERE u.id IN (
            SELECT uc.userId
            FROM Community com
            INNER JOIN UsersCommunities uc on com.id = uc.communityId
            WHERE uc.communityId IN :ids
        )
    """, nativeQuery = true
    )
    fun findAllUsersIdsByBeInCommunities(@Param("ids") communitiesIds: List<Long>): List<Long>
}
