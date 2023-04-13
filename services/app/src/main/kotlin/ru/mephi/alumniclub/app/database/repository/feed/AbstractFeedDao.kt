package ru.mephi.alumniclub.app.database.repository.feed

import org.springframework.data.repository.PagingAndSortingRepository
import ru.mephi.alumniclub.app.database.entity.feed.AbstractFeed
import ru.mephi.alumniclub.shared.database.repository.AbstractRepository

interface AbstractFeedDao : AbstractRepository<AbstractFeed>, PagingAndSortingRepository<AbstractFeed, Long>
