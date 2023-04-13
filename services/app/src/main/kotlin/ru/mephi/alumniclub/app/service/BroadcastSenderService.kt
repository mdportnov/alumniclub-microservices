package ru.mephi.alumniclub.app.service

import ru.mephi.alumniclub.app.database.entity.atom.Atom
import ru.mephi.alumniclub.app.model.dto.broadcast.request.BroadcastByPublicationRequest

interface BroadcastSenderService {
    fun createBroadcast(userId: Long, request: BroadcastByPublicationRequest)
    fun createBroadcast(atom: Atom)
}
