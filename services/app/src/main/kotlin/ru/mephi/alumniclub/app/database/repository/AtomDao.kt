package ru.mephi.alumniclub.app.database.repository

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import ru.mephi.alumniclub.app.database.entity.atom.Atom
import java.util.*

interface AtomDao : CrudRepository<Atom, UUID> {
    fun findAllByUserIdOrderByCreatedAtDesc(id: Long): List<Atom>

    @Query(
        """
        SELECT coalesce(sum((CASE WHEN (a.sign = true) THEN 1 else -1 END) * a.amount), 0)
        FROM Atom a
        WHERE a.user.id = :userId
    """
    )
    fun countAtomsOfUser(@Param("userId") userId: Long): Long
}
