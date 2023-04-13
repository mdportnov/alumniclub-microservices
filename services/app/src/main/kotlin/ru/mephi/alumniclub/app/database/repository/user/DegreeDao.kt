package ru.mephi.alumniclub.app.database.repository.user


import org.springframework.data.jpa.repository.Query
import ru.mephi.alumniclub.app.database.entity.user.Degree
import ru.mephi.alumniclub.shared.database.repository.AbstractRepository

interface DegreeDao : AbstractRepository<Degree> {
    @Query("SELECT degree FROM Degree degree WHERE degree.user.id = :id")
    fun findByUserId(id: Long): List<Degree>
}
