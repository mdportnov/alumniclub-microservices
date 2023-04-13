package ru.mephi.alumniclub.app.database.repository.project

import ru.mephi.alumniclub.app.database.entity.feed.EventFeed
import ru.mephi.alumniclub.app.database.entity.project.AbstractProject
import ru.mephi.alumniclub.app.database.entity.project.Project

interface ProjectDao : AbstractProjectDao<Project> {
    fun findByEventFeedId(id: Long): Project?
    fun findByEventFeed(eventFeed: EventFeed): AbstractProject?
}
