package ru.mephi.alumniclub.app.service.impl

import org.springframework.stereotype.Component
import ru.mephi.alumniclub.app.database.entity.atom.Atom
import ru.mephi.alumniclub.app.database.repository.AtomDao
import ru.mephi.alumniclub.app.model.dto.atom.request.SendAtomsRequest
import ru.mephi.alumniclub.app.model.dto.atom.response.AtomHistoryResponse
import ru.mephi.alumniclub.app.model.exception.common.ResourceNotFoundException
import ru.mephi.alumniclub.app.model.mapper.atom.AtomMapper
import ru.mephi.alumniclub.app.service.AtomService
import ru.mephi.alumniclub.app.service.BroadcastSenderService
import ru.mephi.alumniclub.app.service.UserService
import ru.mephi.alumniclub.shared.util.response.ResponseManager
import java.util.*
import javax.transaction.Transactional
import kotlin.jvm.optionals.getOrNull

@Component
class AtomServiceImpl(
    private val atomDao: AtomDao,
    private val atomMapper: AtomMapper,
    private val userService: UserService,
    private val broadcastService: BroadcastSenderService
) : ResponseManager(), AtomService {
    override fun getUserAtomHistory(userId: Long): List<AtomHistoryResponse> {
        return atomMapper.asResponseList(atomDao.findAllByUserIdOrderByCreatedAtDesc(userId))
    }

    @Transactional
    override fun deleteAtomHistoryById(userId: Long, atomId: UUID): List<AtomHistoryResponse> {
        val atom = findAtomEntityById(atomId)
        atomDao.delete(atom)
        return getUserAtomHistory(userId)
    }

    @Transactional
    override fun accrueAtoms(adminId: Long, atomRequest: SendAtomsRequest): List<AtomHistoryResponse> {
        val user = userService.findUserEntityById(atomRequest.userId)
        val atom = atomMapper.asEntity(user, atomRequest)
        val entity = atomDao.save(atom)

        broadcastService.createBroadcast(entity)
        return getUserAtomHistory(atomRequest.userId)
    }

    override fun findAtomEntityById(id: UUID): Atom {
        return atomDao.findById(id).orElseThrow { ResourceNotFoundException(Atom::class.java, id) }
    }

    @OptIn(ExperimentalStdlibApi::class)
    override fun findAtomEntityOrNullById(id: UUID): Atom? {
        return atomDao.findById(id).getOrNull()
    }

    override fun getUserAtomsCount(userId: Long): Long {
        return atomDao.countAtomsOfUser(userId)
    }
}
