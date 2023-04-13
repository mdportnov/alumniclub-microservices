package ru.mephi.alumniclub.app.model.dto.project.response

import ru.mephi.alumniclub.app.model.dto.feed.response.user.EventShortResponseForUser
import ru.mephi.alumniclub.app.model.dto.feed.response.user.PublicationShortResponseForUser

class ProjectPreviewResponse(
    val publications: List<PublicationShortResponseForUser>,
    val events: List<EventShortResponseForUser>
)
