package ru.mephi.alumniclub.app.database.repository.fcm

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import ru.mephi.alumniclub.app.database.entity.fcm.FcmToken
import ru.mephi.alumniclub.app.database.entity.user.Fingerprint
import java.util.*

@Repository
interface FcmTokenDao : CrudRepository<FcmToken, Long> {

    fun findByFingerprint(fingerprint: Fingerprint): Optional<FcmToken>


    @Query(" SELECT token FROM FcmToken token WHERE token.fingerprint.user.id IN :ids")
    fun getFbTokensByUsersId(@Param("ids") ids: List<Long>): List<FcmToken>

    @Query(
        value = """
        SELECT DISTINCT tokens.*
        FROM Communities AS com
        INNER JOIN UsersCommunities AS uc ON uc.communityId = com.id
        INNER JOIN Users AS users ON uc.userId = users.id
        INNER JOIN FcmToken AS tokens ON tokens.userId = users.id
        WHERE com.id IN :ids
    """, nativeQuery = true
    )
    fun getFbTokensByCommunitiesIds(@Param("ids") list: List<Long>): List<FcmToken>
}