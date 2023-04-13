package ru.mephi.alumniclub.shared.database.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.NoRepositoryBean
import ru.mephi.alumniclub.shared.database.entity.AbstractEntity

@NoRepositoryBean
interface AbstractRepository<E : AbstractEntity> : CrudRepository<E, Long>