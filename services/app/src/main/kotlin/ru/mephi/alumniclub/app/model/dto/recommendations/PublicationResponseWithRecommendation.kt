package ru.mephi.alumniclub.app.model.dto.recommendations

import ru.mephi.alumniclub.app.model.dto.feed.response.FeedResponse
import ru.mephi.alumniclub.app.model.dto.feed.response.user.PublicationResponseForUser
import ru.mephi.alumniclub.app.model.dto.user.response.UserShortResponse
import java.time.LocalDateTime
import java.util.*

class PublicationResponseWithRecommendation(
    id: UUID,
    publicationDate: LocalDateTime,
    title: String,
    content: String,
    author: UserShortResponse?,
    feed: FeedResponse,
    liked: Boolean,
    humanUrl: String,
    likesCount: Long = -1,
    viewsCount: Long,
    photoPath: String? = null,
    val recommendation: ContentBasedRecommendationResponse,
) : PublicationResponseForUser(
    id,
    publicationDate,
    title,
    content,
    author,
    feed,
    liked,
    humanUrl,
    likesCount,
    viewsCount,
    photoPath
)