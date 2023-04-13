package ru.mephi.alumniclub.app.model.mapper.community

import org.springframework.context.annotation.Lazy
import org.springframework.data.domain.Page
import org.springframework.stereotype.Component
import ru.mephi.alumniclub.app.database.entity.community.UserCommunity
import ru.mephi.alumniclub.app.model.dto.PageResponse
import ru.mephi.alumniclub.app.model.dto.community.response.MemberResponse
import ru.mephi.alumniclub.app.model.mapper.user.UserMapper
import ru.mephi.alumniclub.app.service.UserService

@Component
class UserCommunityMapper(
    private val userMapper: UserMapper,
    @Lazy private val userService: UserService
) {
    fun asResponse(userCommunity: UserCommunity): MemberResponse {
        return MemberResponse(
            userCommunity.id,
            userCommunity.createdAt,
            userCommunity.user.mentor,
            userMapper.asPreviewResponse(userService.findUserEntityById(userCommunity.user.id))
        )
    }

    fun asPageResponse(users: Page<UserCommunity>): PageResponse<MemberResponse> {
        return PageResponse(
            users.content.map { asResponse(it) },
            users.number.toLong(),
            users.numberOfElements.toLong(),
            users.totalPages.toLong()
        )
    }
}
