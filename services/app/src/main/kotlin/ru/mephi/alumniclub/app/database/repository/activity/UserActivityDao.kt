package ru.mephi.alumniclub.app.database.repository.activity

import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import ru.mephi.alumniclub.app.database.entity.activity.UserActivity
import java.time.LocalDateTime

@Repository
interface UserActivityDao : CrudRepository<UserActivity, Long> {
    @Query("select count (distinct ua.id) from UserActivity as ua where ua.time < :before and ua.time > :after")
    fun countDistinctUserByTimeAfterAndTimeBefore(after: LocalDateTime, before: LocalDateTime): Long

    @Modifying
    @Transactional
    @Query("update UserActivity ua set ua.time = current_timestamp() where ua.id = :userId")
    fun update(userId: Long)
}
