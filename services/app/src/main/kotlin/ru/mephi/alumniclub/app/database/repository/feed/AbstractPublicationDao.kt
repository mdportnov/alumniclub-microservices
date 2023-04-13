package ru.mephi.alumniclub.app.database.repository.feed

import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository
import ru.mephi.alumniclub.app.database.entity.publication.AbstractPublication
import java.util.*

@Repository
interface AbstractPublicationDao : PagingAndSortingRepository<AbstractPublication, UUID>
