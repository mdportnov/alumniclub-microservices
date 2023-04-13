package ru.mephi.alumniclub.shared.database.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.NoRepositoryBean

@NoRepositoryBean
interface CommonRepositoryWithUser<E> : CrudRepository<E, Long>