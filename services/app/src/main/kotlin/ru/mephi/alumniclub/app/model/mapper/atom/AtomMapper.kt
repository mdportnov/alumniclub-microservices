package ru.mephi.alumniclub.app.model.mapper.atom

import org.springframework.stereotype.Component
import ru.mephi.alumniclub.app.database.entity.atom.Atom
import ru.mephi.alumniclub.app.database.entity.user.User
import ru.mephi.alumniclub.app.model.dto.atom.request.SendAtomsRequest
import ru.mephi.alumniclub.app.model.dto.atom.response.AtomHistoryResponse

@Component
class AtomMapper {
    fun asEntity(user: User, request: SendAtomsRequest): Atom {
        return Atom(
            user = user,
            description = request.description,
            amount = request.amount,
            sign = request.sign,
        )
    }

    fun asResponse(atom: Atom): AtomHistoryResponse {
        return AtomHistoryResponse(
            id = atom.id,
            createdAt = atom.createdAt,
            sign = atom.sign,
            amount = atom.amount,
            description = atom.description
        )
    }

    fun asResponseList(atoms: List<Atom>): List<AtomHistoryResponse> = atoms.map(::asResponse)
}
