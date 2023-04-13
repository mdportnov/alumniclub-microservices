package ru.mephi.alumniclub.app.model.mapper.atom;

import org.springframework.stereotype.Component
import ru.mephi.alumniclub.app.database.entity.atom.Merch
import ru.mephi.alumniclub.app.model.dto.atom.request.MerchRequest
import ru.mephi.alumniclub.app.model.dto.atom.response.MerchResponse

@Component
class MerchMapper {
    fun asEntity(dto: MerchRequest): Merch {
        return Merch(
            name = dto.name,
            description = dto.description,
            cost = dto.cost,
            availability = dto.availability
        )
    }

    fun update(entity: Merch, request: MerchRequest): Merch {
        entity.name = request.name
        entity.description = request.description
        entity.cost = request.cost
        entity.availability = request.availability
        return entity
    }

    fun asResponse(entity: Merch): MerchResponse {
        return MerchResponse(
            id = entity.id,
            name = entity.name,
            description = entity.description,
            cost = entity.cost,
            availability = entity.availability,
            photoPath = entity.photoPath
        )
    }

    fun asResponseList(entityList: List<Merch>) = entityList.map(::asResponse)
}
