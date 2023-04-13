package ru.mephi.alumniclub.app.service

import io.swagger.v3.oas.annotations.Parameter
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import ru.mephi.alumniclub.app.database.entity.atom.Atom
import ru.mephi.alumniclub.app.model.dto.atom.request.SendAtomsRequest
import ru.mephi.alumniclub.app.model.dto.atom.response.AtomHistoryResponse
import java.util.*


interface AtomService {
    fun getUserAtomHistory(@RequestParam userId: Long): List<AtomHistoryResponse>
    fun deleteAtomHistoryById(@RequestParam userId: Long, @RequestParam atomId: UUID): List<AtomHistoryResponse>
    fun accrueAtoms(
        @Parameter(hidden = true) adminId: Long,
        @RequestBody atomRequest: SendAtomsRequest
    ): List<AtomHistoryResponse>

    fun findAtomEntityById(id: UUID): Atom
    fun findAtomEntityOrNullById(id: UUID): Atom?

    fun getUserAtomsCount(userId: Long): Long
}