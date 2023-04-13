package ru.mephi.alumniclub.app.service

import ru.mephi.alumniclub.app.database.entity.feed.AbstractFeed
import ru.mephi.alumniclub.app.database.entity.feed.EventFeed
import ru.mephi.alumniclub.app.database.entity.feed.PublicationFeed
import ru.mephi.alumniclub.app.model.dto.PageResponse
import ru.mephi.alumniclub.app.model.dto.feed.response.FeedResponse
import ru.mephi.alumniclub.app.model.enumeration.NotificationCategory
import ru.mephi.alumniclub.shared.dto.ExtendedPageRequest

interface FeedService {
    fun existsFeedIdInProjects(feedId: Long, projectIds: List<Long>): Boolean
    fun existsPublicationFeedById(id: Long): Boolean
    fun existsEventFeedById(id: Long): Boolean
    fun listPublicationFeeds(query: String, pageRequest: ExtendedPageRequest): PageResponse<FeedResponse>
    fun findAbstractFeedEntityById(id: Long): AbstractFeed
    fun findPublicationFeedEntityById(id: Long): PublicationFeed
    fun findEventFeedEntityById(id: Long): EventFeed
    fun createPublicationFeedEntity(name: String, category: NotificationCategory): PublicationFeed
    fun createEventFeedEntity(name: String): EventFeed
    fun update(feed: PublicationFeed, name: String): PublicationFeed
    fun update(feed: EventFeed, name: String): EventFeed
}
