package ru.mephi.alumniclub.app.database.repository.partnership

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import ru.mephi.alumniclub.app.database.entity.partnership.Partnership
import ru.mephi.alumniclub.shared.database.repository.AbstractRepository
import java.time.LocalDateTime

interface PartnershipDao : AbstractRepository<Partnership> {
    fun findAll(pageable: Pageable): Page<Partnership>
    fun findByCurrentUntilIsAfter(pageable: Pageable, time: LocalDateTime = LocalDateTime.now()): Page<Partnership>
}