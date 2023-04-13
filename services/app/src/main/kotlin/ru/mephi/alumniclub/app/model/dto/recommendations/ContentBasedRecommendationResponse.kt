package ru.mephi.alumniclub.app.model.dto.recommendations

import ru.mephi.alumniclub.app.model.dto.PageResponse
import ru.mephi.alumniclub.app.model.dto.feed.response.user.PublicationShortResponseForUser

class ContentBasedRecommendationResponse(
    val page: PageResponse<PublicationShortResponseForUser>
)