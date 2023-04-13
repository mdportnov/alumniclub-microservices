package ru.mephi.alumniclub.app.model.mapper.feed

import org.springframework.data.domain.Page
import org.springframework.stereotype.Component
import ru.mephi.alumniclub.app.database.entity.feed.AbstractFeed
import ru.mephi.alumniclub.app.database.entity.feed.PublicationFeed
import ru.mephi.alumniclub.app.database.entity.project.AbstractProject
import ru.mephi.alumniclub.app.database.repository.project.AbstractProjectDao
import ru.mephi.alumniclub.app.model.dto.PageResponse
import ru.mephi.alumniclub.app.model.dto.feed.response.FeedResponse
import ru.mephi.alumniclub.app.model.dto.project.IdColorProjection

@Component
class FeedMapper(
    private val projectDao: AbstractProjectDao<AbstractProject>
) {
    fun <T : AbstractFeed> update(feed: T, name: String): T {
        feed.name = name
        return feed
    }

    fun <T : AbstractFeed> asResponse(feed: T): FeedResponse {
        val (projectId, projectColor) = projectDao.findIdAndColorByFeedId(feed.id) ?: IdColorProjection.empty()
        return FeedResponse(
            id = feed.id,
            name = feed.name,
            projectId = projectId,
            color = projectColor
        )
    }

    fun asPageResponse(page: Page<PublicationFeed>): PageResponse<FeedResponse> {
        return PageResponse(
            content = page.content.map(::asResponse),
            number = page.number.toLong(),
            numberOfElements = page.numberOfElements.toLong(),
            totalPages = page.totalPages.toLong()
        )
    }
}
