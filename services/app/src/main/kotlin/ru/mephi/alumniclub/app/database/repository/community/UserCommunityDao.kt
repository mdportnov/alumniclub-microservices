package ru.mephi.alumniclub.app.database.repository.community

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import ru.mephi.alumniclub.app.database.entity.community.Community
import ru.mephi.alumniclub.app.database.entity.community.UserCommunity
import ru.mephi.alumniclub.app.database.entity.user.User
import ru.mephi.alumniclub.shared.database.repository.AbstractRepository
import java.util.*

interface UserCommunityDao : AbstractRepository<UserCommunity> {
    fun existsByUserAndCommunity(user: User, community: Community): Boolean
    fun existsByUserIdAndCommunity(userId: Long, community: Community): Boolean
    fun findByUserAndCommunity(user: User, community: Community): Optional<UserCommunity>
    fun findByUserId(userId: Long, pageable: Pageable): Page<UserCommunity>
    fun findByUserIdAndCommunityHiddenFalse(userId: Long, pageable: Pageable): Page<UserCommunity>
    fun findByCommunityAndUserSurnameStartsWith(
        community: Community,
        prefix: String,
        pageable: Pageable
    ): Page<UserCommunity>

    fun findByCommunityAndUserSurnameStartsWithAndUserBannedFalse(
        community: Community,
        prefix: String,
        pageable: Pageable
    ): Page<UserCommunity>

    fun findByCommunityIdIn(ids: List<Long>): List<UserCommunity>
    fun countByCommunityIdAndUserBannedFalse(communityId: Long): Long
    fun deleteByUserAndCommunity(user: User, community: Community)
}
