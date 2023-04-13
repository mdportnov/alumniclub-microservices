package ru.mephi.alumniclub.app.model.mapper.feed

import org.springframework.data.domain.Page
import org.springframework.stereotype.Component
import ru.mephi.alumniclub.app.database.entity.publication.Like
import ru.mephi.alumniclub.app.model.dto.PageResponse
import ru.mephi.alumniclub.app.model.dto.feed.response.LikeResponse
import ru.mephi.alumniclub.app.model.mapper.user.UserMapper

@Component
class LikeMapper(
    private val userMapper: UserMapper
) {
    fun asPageResponse(likes: Page<Like>): PageResponse<LikeResponse> {
        return PageResponse(
            content = likes.content.map(::asResponse),
            number = likes.number.toLong(),
            numberOfElements = likes.numberOfElements.toLong(),
            totalPages = likes.totalPages.toLong()
        )
    }

    private fun asResponse(like: Like): LikeResponse {
        return LikeResponse(
            id = like.id,
            createdAt = like.createdAt,
            user = userMapper.asShortResponse(like.user)
        )
    }
}
