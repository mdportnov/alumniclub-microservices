package ru.mephi.alumniclub.app.model.dto.project.request

import ru.mephi.alumniclub.app.model.enumeration.ProjectType

class ProjectCreateRequest(
    val type: ProjectType,
    name: String,
    description: String,
    hiddenCommunity: Boolean
) : ProjectRequest(
    name = name,
    description = description,
    hiddenCommunity = hiddenCommunity
)
