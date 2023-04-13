package ru.mephi.alumniclub.app.service

import ru.mephi.alumniclub.app.database.entity.publication.Event
import ru.mephi.alumniclub.app.database.entity.publication.Join
import ru.mephi.alumniclub.app.database.entity.user.User
import ru.mephi.alumniclub.app.model.dto.meeting.MeetingParticipationRequest

interface JoinService {
    fun join(event: Event, user: User): Join
    fun join(event: Event, user: User, request: MeetingParticipationRequest): Join
    fun leave(event: Event, user: User): Long
    fun getJoinsCount(event: Event): Long
    fun existsByEventAndUserId(event: Event, userId: Long): Boolean
}
