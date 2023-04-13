package ru.mephi.alumniclub.app.model.mapper.user

import org.springframework.stereotype.Component
import ru.mephi.alumniclub.app.database.entity.user.BioVisibility
import ru.mephi.alumniclub.app.database.entity.user.User
import ru.mephi.alumniclub.app.database.entity.user.UserVisibility
import ru.mephi.alumniclub.app.model.dto.user.UserVisibilityDTO

@Component
class UserVisibilityMapper(
    private val bioVisibilityMapper: BioVisibilityMapper
) {
    fun asEntity(user: User, request: UserVisibilityDTO) = UserVisibility(
        user = user,
        email = request.email,
        phone = request.phone,
        vk = request.vk,
        tg = request.tg,
        gender = request.gender,
        birthday = request.birthday,
        degrees = request.degrees,
        createdAt = request.createdAt,
    )

    fun update(entity: UserVisibility, request: UserVisibilityDTO): UserVisibility {
        entity.email = request.email
        entity.phone = request.phone
        entity.vk = request.vk
        entity.tg = request.tg
        entity.gender = request.gender
        entity.birthday = request.birthday
        entity.degrees = request.degrees
        entity.createdAt = request.createdAt
        return entity
    }

    fun asResponse(entity: UserVisibility, bioVisibility: BioVisibility): UserVisibilityDTO {
        return UserVisibilityDTO(
            email = entity.email,
            phone = entity.phone,
            vk = entity.vk,
            tg = entity.tg,
            gender = entity.gender,
            birthday = entity.birthday,
            createdAt = entity.createdAt,
            bioVisibility = bioVisibilityMapper.asResponse(bioVisibility),
            degrees = entity.degrees,
        )
    }
}
