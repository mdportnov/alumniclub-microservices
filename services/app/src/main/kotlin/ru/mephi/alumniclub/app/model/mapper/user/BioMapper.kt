package ru.mephi.alumniclub.app.model.mapper.user

import org.springframework.stereotype.Component
import ru.mephi.alumniclub.app.database.entity.user.BioVisibility
import ru.mephi.alumniclub.app.database.entity.user.Biography
import ru.mephi.alumniclub.app.database.entity.user.User
import ru.mephi.alumniclub.app.model.dto.user.request.BioRequest
import ru.mephi.alumniclub.app.model.dto.user.response.BioResponse

@Component
class BioMapper {
    fun asEntity(user: User, request: BioRequest): Biography {
        return Biography(
            user,
            request.country,
            request.city,
            request.jobArea,
            request.company,
            request.job,
            request.experience,
            request.hobbies,
        )
    }

    fun update(entity: Biography, request: BioRequest): Biography {
        entity.country = request.country
        entity.city = request.city
        entity.jobArea = request.jobArea
        entity.company = request.company
        entity.job = request.job
        entity.experience = request.experience
        entity.hobbies = request.hobbies
        return entity
    }

    fun asResponse(entity: Biography): BioResponse {
        return BioResponse(
            entity.country,
            entity.city,
            entity.jobArea,
            entity.company,
            entity.job,
            entity.experience,
            entity.hobbies,
        )
    }

    fun asMaskedResponse(bio: Biography, bioVisibility: BioVisibility): BioResponse {
        return BioResponse(
            if (bioVisibility.country) bio.country else null,
            if (bioVisibility.city) bio.city else null,
            if (bioVisibility.jobArea) bio.jobArea else null,
            if (bioVisibility.company) bio.company else null,
            if (bioVisibility.job) bio.job else null,
            if (bioVisibility.experience) bio.experience else null,
            if (bioVisibility.hobbies) bio.hobbies else null,
        )
    }
}
