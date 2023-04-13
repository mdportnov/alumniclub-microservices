package ru.mephi.alumniclub.app.model.mapper.feed

import org.springframework.data.domain.Page
import org.springframework.stereotype.Component
import ru.mephi.alumniclub.app.database.entity.publication.Publication
import ru.mephi.alumniclub.app.model.dto.CursorResponse
import ru.mephi.alumniclub.app.model.dto.PageResponse
import ru.mephi.alumniclub.app.model.dto.feed.request.PublicationRequest
import ru.mephi.alumniclub.app.model.dto.feed.request.PublicationUpdateRequest
import ru.mephi.alumniclub.app.model.dto.feed.response.admin.PublicationResponseForAdmin
import ru.mephi.alumniclub.app.model.dto.feed.response.admin.PublicationShortResponseForAdmin
import ru.mephi.alumniclub.app.model.dto.feed.response.user.PublicationResponseForUser
import ru.mephi.alumniclub.app.model.dto.feed.response.user.PublicationShortResponseForUser
import ru.mephi.alumniclub.app.model.dto.recommendations.ContentBasedRecommendationResponse
import ru.mephi.alumniclub.app.model.dto.recommendations.PublicationResponseWithRecommendation
import ru.mephi.alumniclub.app.model.mapper.user.UserMapper
import ru.mephi.alumniclub.app.service.LikeService
import ru.mephi.alumniclub.app.util.parseArticle

@Component
class PublicationMapper(
    private val userMapper: UserMapper,
    private val feedMapper: FeedMapper,
    private val likeService: LikeService
) {
    fun asEntity(request: PublicationRequest): Publication {
        return Publication(
            title = request.title,
            content = request.content,
            humanUrl = request.humanUrl,
            publicationDate = request.publicationDate,
            hidden = request.hidden
        )
    }

    fun update(publication: Publication, request: PublicationUpdateRequest): Publication {
        publication.title = request.title
        publication.content = request.content
        publication.humanUrl = request.humanUrl
        publication.hidden = request.hidden
        publication.publicationDate = request.publicationDate
        return publication
    }

    fun asResponseForPublic(publication: Publication): PublicationResponseForUser {
        return PublicationResponseForUser(
            id = publication.id,
            publicationDate = publication.publicationDate,
            title = publication.title,
            content = publication.content,
            author = userMapper.asNullableShortResponse(publication.author),
            feed = feedMapper.asResponse(publication.feed),
            liked = false,
            humanUrl = publication.humanUrl,
            likesCount = likeService.getLikesCount(publication),
            viewsCount = publication.viewsCount,
            photoPath = publication.photoPath
        )
    }

    fun asResponseForUser(
        response: PublicationResponseForUser, recommendation: ContentBasedRecommendationResponse
    ): PublicationResponseWithRecommendation {
        return PublicationResponseWithRecommendation(
            id = response.id,
            publicationDate = response.createdAt,
            title = response.title,
            content = response.content,
            author = response.author,
            feed = response.feed,
            liked = response.liked,
            humanUrl = response.humanUrl,
            likesCount = response.likesCount,
            viewsCount = response.viewsCount,
            photoPath = response.photoPath,
            recommendation = recommendation
        )
    }

    fun asResponseForUser(userId: Long, publication: Publication): PublicationResponseForUser {
        return PublicationResponseForUser(
            id = publication.id,
            publicationDate = publication.publicationDate,
            title = publication.title,
            content = publication.content,
            author = userMapper.asNullableShortResponse(publication.author),
            feed = feedMapper.asResponse(publication.feed),
            liked = likeService.existsByAbstractPublicationAndUserId(publication, userId),
            humanUrl = publication.humanUrl,
            likesCount = likeService.getLikesCount(publication),
            viewsCount = publication.viewsCount,
            photoPath = publication.photoPath
        )
    }

    fun asResponseForAdmin(publication: Publication): PublicationResponseForAdmin {
        return PublicationResponseForAdmin(
            id = publication.id,
            createdAt = publication.createdAt,
            publicationDate = publication.publicationDate,
            title = publication.title,
            content = publication.content,
            author = userMapper.asNullableShortResponse(publication.author),
            feed = feedMapper.asResponse(publication.feed),
            humanUrl = publication.humanUrl,
            likesCount = likeService.getLikesCount(publication),
            viewsCount = publication.viewsCount,
            hidden = publication.hidden,
            photoPath = publication.photoPath
        )
    }

    fun asPageResponseForPublic(publications: Page<Publication>): PageResponse<PublicationShortResponseForUser> {
        return PageResponse(
            content = publications.map { asShortResponseForPublic(it) }.content,
            number = publications.number.toLong(),
            numberOfElements = publications.numberOfElements.toLong(),
            totalPages = publications.totalPages.toLong()
        )
    }

    fun asPageResponseForUser(
        userId: Long, publications: Page<Publication>
    ): PageResponse<PublicationShortResponseForUser> {
        return PageResponse(
            content = publications.content.map { asShortResponseForUser(userId, it) },
            number = publications.number.toLong(),
            numberOfElements = publications.numberOfElements.toLong(),
            totalPages = publications.totalPages.toLong()
        )
    }

    fun asCursorResponseForUser(
        userId: Long, publications: Page<Publication>
    ): CursorResponse<PublicationShortResponseForUser> {
        return CursorResponse(
            content = publications.content.map { asShortResponseForUser(userId, it) },
            numberOfElements = publications.numberOfElements.toLong()
        )
    }

    fun asPageResponseForAdmin(publications: Page<Publication>): PageResponse<PublicationShortResponseForAdmin> {
        return PageResponse(
            content = publications.content.map { asShortResponseForAdmin(it) },
            number = publications.number.toLong(),
            numberOfElements = publications.numberOfElements.toLong(),
            totalPages = publications.totalPages.toLong()
        )
    }

    private fun asShortResponseForPublic(publication: Publication): PublicationShortResponseForUser {
        return PublicationShortResponseForUser(
            id = publication.id,
            publicationDate = publication.publicationDate,
            title = publication.title,
            content = parseArticle(publication.content),
            feed = feedMapper.asResponse(publication.feed),
            liked = false,
            humanUrl = publication.humanUrl,
            likesCount = likeService.getLikesCount(publication),
            viewsCount = publication.viewsCount,
            photoPath = publication.photoPath
        )
    }

    private fun asShortResponseForUser(userId: Long, publication: Publication): PublicationShortResponseForUser {
        return PublicationShortResponseForUser(
            id = publication.id,
            publicationDate = publication.publicationDate,
            title = publication.title,
            content = parseArticle(publication.content),
            feed = feedMapper.asResponse(publication.feed),
            liked = likeService.existsByAbstractPublicationAndUserId(publication, userId),
            humanUrl = publication.humanUrl,
            likesCount = likeService.getLikesCount(publication),
            viewsCount = publication.viewsCount,
            photoPath = publication.photoPath
        )
    }

    private fun asShortResponseForAdmin(publication: Publication): PublicationShortResponseForAdmin {
        return PublicationShortResponseForAdmin(
            id = publication.id,
            createdAt = publication.createdAt,
            publicationDate = publication.publicationDate,
            title = publication.title,
            feed = feedMapper.asResponse(publication.feed),
            humanUrl = publication.humanUrl,
            likesCount = likeService.getLikesCount(publication),
            viewsCount = publication.viewsCount,
            hidden = publication.hidden,
            photoPath = publication.photoPath
        )
    }
}
