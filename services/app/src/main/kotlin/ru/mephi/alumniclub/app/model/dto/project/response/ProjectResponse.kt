package ru.mephi.alumniclub.app.model.dto.project.response

import ru.mephi.alumniclub.app.model.dto.community.response.CommunityResponse
import ru.mephi.alumniclub.app.model.dto.feed.response.FeedResponse
import ru.mephi.alumniclub.app.model.enumeration.ProjectType
import ru.mephi.alumniclub.shared.dto.AbstractCreatedAtResponse
import ru.mephi.alumniclub.shared.dto.photo.PhotoPathed
import java.time.LocalDateTime

class ProjectResponse(
    id: Long,
    createdAt: LocalDateTime,

    val type: ProjectType,
    val name: String,
    val description: String,
    val archive: Boolean,
    var joined: Boolean,
    override var photoPath: String?,
    var community: CommunityResponse,
    var publicationFeed: FeedResponse,
    var eventFeed: FeedResponse?,
    val color: String? = null,
) : AbstractCreatedAtResponse<Long>(id, createdAt), PhotoPathed
