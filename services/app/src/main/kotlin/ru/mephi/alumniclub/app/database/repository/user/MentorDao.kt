package ru.mephi.alumniclub.app.database.repository.user

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import ru.mephi.alumniclub.app.database.entity.user.MentorData
import ru.mephi.alumniclub.shared.database.repository.CommonRepositoryWithUser
import java.util.*

interface MentorDao : CommonRepositoryWithUser<MentorData> {
    fun deleteByUserId(userId: Long): Int
    fun existsByUserId(userId: Long): Boolean
    fun findByUserId(userId: Long): MentorData?
    fun findByUserSurnameStartsWithOrderByAvailableDesc(prefix: String, pageable: Pageable): Page<MentorData>
    fun findByUserSurnameStartsWithAndAvailableIsTrue(prefix: String, pageable: Pageable): Page<MentorData>
}
