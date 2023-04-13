package ru.mephi.alumniclub.app.database.repository.fcm

import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import ru.mephi.alumniclub.app.database.entity.fcm.ErroredFcmToken

@Repository
interface ErroredFcmTokenDao : CrudRepository<ErroredFcmToken, Long> {

    @Transactional
    @Modifying
    @Query("DELETE FROM ErroredFcmToken e WHERE e.id = ?1")
    override fun deleteById(id: Long)
}