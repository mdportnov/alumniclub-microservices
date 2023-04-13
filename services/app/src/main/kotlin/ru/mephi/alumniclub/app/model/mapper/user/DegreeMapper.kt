package ru.mephi.alumniclub.app.model.mapper.user

import org.springframework.stereotype.Component
import ru.mephi.alumniclub.app.database.entity.user.Degree
import ru.mephi.alumniclub.app.database.entity.user.User
import ru.mephi.alumniclub.app.model.dto.user.DegreeDTO

@Component
class DegreeMapper {
    fun asEntity(user: User, dto: DegreeDTO): Degree {
        val degree = Degree(
            dto.enrollment, dto.graduation, dto.degree, dto.description
        )
        degree.user = user
        degree.createdAt = user.createdAt
        return degree
    }

    fun asDTO(entity: Degree): DegreeDTO {
        return DegreeDTO(
            entity.enrollment, entity.graduation, entity.degreeType, entity.description
        )
    }

    fun asDTOList(entities: List<Degree>) = entities.map(::asDTO)
}
