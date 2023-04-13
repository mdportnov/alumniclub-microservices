package ru.mephi.alumniclub.app.model.dto.feed.response

import ru.mephi.alumniclub.shared.dto.AbstractResponse

class FeedResponse(
    id: Long,
    val name: String?,
    var projectId: Long?,
    var color: String? = null,
) : AbstractResponse<Long>(id)
