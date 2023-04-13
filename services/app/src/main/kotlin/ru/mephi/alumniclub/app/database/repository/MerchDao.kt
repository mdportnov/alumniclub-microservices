package ru.mephi.alumniclub.app.database.repository

import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.transaction.annotation.Transactional
import ru.mephi.alumniclub.app.database.entity.atom.Merch
import ru.mephi.alumniclub.shared.database.repository.AbstractRepository
import java.util.*

interface MerchDao : AbstractRepository<Merch> {
    fun findAllByAvailabilityIsTrue(): List<Merch>

    fun findByIdAndAvailabilityIsTrue(id: Long): Optional<Merch>

    @Transactional
    @Modifying
    fun deleteMerchById(id: Long)

    @Modifying
    @Transactional
    @Query(
        "update #{#entityName} m set m.availability = :availability, m.cost = :cost,"
                + " m.description = :description, m.name = :name where m.id = :id"
    )
    fun update(id: Long, availability: Boolean, cost: Int, description: String, name: String)
}
