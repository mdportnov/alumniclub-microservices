package ru.mephi.alumniclub.imageservice.database.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import ru.mephi.alumniclub.imageservice.database.entity.publication.AbstractPublication
import java.util.*

@Repository
interface AbstractPublicationDao : CrudRepository<AbstractPublication, UUID>