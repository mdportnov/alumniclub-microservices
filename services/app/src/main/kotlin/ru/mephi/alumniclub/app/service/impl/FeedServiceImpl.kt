package ru.mephi.alumniclub.app.service.impl

import org.springframework.context.annotation.Lazy
import org.springframework.data.jpa.repository.Modifying
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import ru.mephi.alumniclub.app.database.entity.feed.AbstractFeed
import ru.mephi.alumniclub.app.database.entity.feed.EventFeed
import ru.mephi.alumniclub.app.database.entity.feed.PublicationFeed
import ru.mephi.alumniclub.app.database.entity.project.AbstractProject
import ru.mephi.alumniclub.app.database.repository.feed.AbstractFeedDao
import ru.mephi.alumniclub.app.database.repository.feed.EventFeedDao
import ru.mephi.alumniclub.app.database.repository.feed.PublicationFeedDao
import ru.mephi.alumniclub.app.database.repository.project.AbstractProjectDao
import ru.mephi.alumniclub.app.model.dto.PageResponse
import ru.mephi.alumniclub.app.model.dto.feed.response.FeedResponse
import ru.mephi.alumniclub.app.model.enumeration.NotificationCategory
import ru.mephi.alumniclub.app.model.enumeration.feed.DefaultFeed
import ru.mephi.alumniclub.app.model.exception.common.ResourceNotFoundException
import ru.mephi.alumniclub.app.model.mapper.feed.FeedMapper
import ru.mephi.alumniclub.app.service.FeedService
import ru.mephi.alumniclub.app.service.ProjectService
import ru.mephi.alumniclub.shared.dto.ExtendedPageRequest
import ru.mephi.alumniclub.shared.dto.common.ApiError
import ru.mephi.alumniclub.shared.util.response.ResponseManager
import javax.transaction.Transactional

@Service
@Transactional
class FeedServiceImpl(
    private val publicationFeedDao: PublicationFeedDao,
    private val eventFeedDao: EventFeedDao,
    private val abstractFeedDao: AbstractFeedDao,
    private val abstractProjectDao: AbstractProjectDao<AbstractProject>,
    private val feedMapper: FeedMapper
) : ResponseManager(), FeedService {

    init {
        DefaultFeed.values().forEach {
            val name = it.category.title
            val feed = publicationFeedDao.findByName(name)
                .orElseGet { publicationFeedDao.save(PublicationFeed(name = name, category = it.category)) }
            it.id = feed.id
        }
    }

    override fun existsFeedIdInProjects(feedId: Long, projectIds: List<Long>) =
        abstractProjectDao.existsFeedIdInProjects(feedId, projectIds)

    override fun existsPublicationFeedById(id: Long): Boolean {
        return publicationFeedDao.existsById(id)
    }

    override fun existsEventFeedById(id: Long): Boolean {
        return eventFeedDao.existsById(id)
    }

    override fun listPublicationFeeds(query: String, pageRequest: ExtendedPageRequest): PageResponse<FeedResponse> {
        val feeds = publicationFeedDao.findByNameStartsWith(query, pageRequest.pageable)
        return feedMapper.asPageResponse(feeds)
    }


    override fun findAbstractFeedEntityById(id: Long): AbstractFeed {
        return abstractFeedDao.findById(id).orElseThrow { ResourceNotFoundException(AbstractFeed::class.java, id) }
    }

    override fun findPublicationFeedEntityById(id: Long): PublicationFeed {
        return publicationFeedDao.findById(id)
            .orElseThrow { ResourceNotFoundException(PublicationFeed::class.java, id) }
    }

    override fun findEventFeedEntityById(id: Long): EventFeed {
        return eventFeedDao.findById(id).orElseThrow {
            ResourceNotFoundException(EventFeed::class.java, id)
        }
    }

    override fun createPublicationFeedEntity(name: String, category: NotificationCategory): PublicationFeed {
        if (publicationFeedDao.existsByName(name))
            throw ApiError(HttpStatus.CONFLICT, i18n("exception.alreadyExists.publicationFeed", name))
        return publicationFeedDao.save(PublicationFeed(name, category))
    }

    override fun createEventFeedEntity(name: String): EventFeed {
        if (eventFeedDao.existsByName(name))
            throw ApiError(HttpStatus.CONFLICT, i18n("exception.alreadyExists.eventFeed", name))
        return eventFeedDao.save(EventFeed(name))
    }

    @Modifying
    override fun update(feed: PublicationFeed, name: String): PublicationFeed {
        if (publicationFeedDao.existsByNameAndIdNot(name, feed.id))
            throw ApiError(HttpStatus.CONFLICT, i18n("exception.alreadyExists.publicationFeed", name))
        return feedMapper.update(feed, name)
    }

    @Modifying
    override fun update(feed: EventFeed, name: String): EventFeed {
        if (eventFeedDao.existsByNameAndIdNot(name, feed.id))
            throw ApiError(HttpStatus.CONFLICT, i18n("exception.alreadyExists.eventFeed", name))
        return feedMapper.update(feed, name)
    }
}
