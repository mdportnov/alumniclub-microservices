package ru.mephi.alumniclub.app.model.mapper.user

import org.springframework.stereotype.Component
import ru.mephi.alumniclub.app.database.entity.user.BioVisibility
import ru.mephi.alumniclub.app.database.entity.user.Biography
import ru.mephi.alumniclub.app.model.dto.user.BioVisibilityDTO

@Component
class BioVisibilityMapper {
    fun asEntity(bio: Biography, request: BioVisibilityDTO): BioVisibility {
        return BioVisibility(
            bio,
            request.country,
            request.city,
            request.jobArea,
            request.company,
            request.job,
            request.experience,
            request.hobbies,
        )
    }

    fun update(entity: BioVisibility, request: BioVisibilityDTO): BioVisibility {
        entity.country = request.country
        entity.city = request.city
        entity.jobArea = request.jobArea
        entity.company = request.company
        entity.job = request.job
        entity.experience = request.experience
        entity.hobbies = request.hobbies
        return entity
    }

    fun asResponse(entity: BioVisibility): BioVisibilityDTO {
        return BioVisibilityDTO(
            entity.country,
            entity.city,
            entity.jobArea,
            entity.company,
            entity.job,
            entity.experience,
            entity.hobbies,
        )
    }
}
