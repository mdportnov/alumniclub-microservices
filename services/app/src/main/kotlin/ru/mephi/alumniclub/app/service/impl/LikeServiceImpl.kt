package ru.mephi.alumniclub.app.service.impl

import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import ru.mephi.alumniclub.app.database.entity.publication.AbstractPublication
import ru.mephi.alumniclub.app.database.entity.publication.Like
import ru.mephi.alumniclub.app.database.entity.user.User
import ru.mephi.alumniclub.app.database.repository.feed.LikeDao
import ru.mephi.alumniclub.app.model.exception.common.ResourceNotFoundException
import ru.mephi.alumniclub.app.service.LikeService
import ru.mephi.alumniclub.shared.dto.common.ApiError
import ru.mephi.alumniclub.shared.util.constants.LIKES_COUNT_CACHE
import ru.mephi.alumniclub.shared.util.response.ResponseManager

@Service
class LikeServiceImpl(
    private val likeDao: LikeDao
) : ResponseManager(), LikeService {

    @CachePut(value = [LIKES_COUNT_CACHE], key = "#publication.id")
    override fun like(publication: AbstractPublication, user: User): Long {
        if (existsByAbstractPublicationAndUserId(publication, user.id))
            throw ApiError(HttpStatus.FORBIDDEN, i18n("exception.alreadyExists.like"))
        likeDao.save(Like(user, publication.id))
        return getLikesCount(publication)
    }

    @CachePut(value = [LIKES_COUNT_CACHE], key = "#publication.id")
    override fun dislike(publication: AbstractPublication, user: User): Long {
        if (!existsByAbstractPublicationAndUserId(publication, user.id))
            throw ResourceNotFoundException(Like::class.java)
        likeDao.deleteByPublicationIdAndUser(publication.id, user)
        return getLikesCount(publication)
    }

    @Cacheable(value = [LIKES_COUNT_CACHE], key = "#publication.id")
    override fun getLikesCount(publication: AbstractPublication): Long {
        return likeDao.countByPublicationId(publication.id)
    }

    override fun existsByAbstractPublicationAndUserId(publication: AbstractPublication, userId: Long): Boolean {
        return likeDao.existsByPublicationIdAndUserId(publication.id, userId)
    }
}
