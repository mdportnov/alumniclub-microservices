package ru.mephi.alumniclub.app.service

import ru.mephi.alumniclub.app.database.entity.publication.AbstractPublication
import ru.mephi.alumniclub.app.database.entity.user.User

interface LikeService {
    fun like(publication: AbstractPublication, user: User): Long
    fun dislike(publication: AbstractPublication, user: User): Long
    fun getLikesCount(publication: AbstractPublication): Long
    fun existsByAbstractPublicationAndUserId(publication: AbstractPublication, userId: Long): Boolean
}
