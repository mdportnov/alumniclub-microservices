package ru.mephi.alumniclub.app.service.impl.push

import org.springframework.stereotype.Component
import ru.mephi.alumniclub.app.database.entity.fcm.ErroredFcmToken
import ru.mephi.alumniclub.app.database.repository.fcm.ErroredFcmTokenDao
import ru.mephi.alumniclub.app.service.ErroredFcmTokenService

@Component
class ErroredFcmTokenServiceImpl(
    private val dao: ErroredFcmTokenDao
) : ErroredFcmTokenService {
    override fun saveAll(list: List<ErroredFcmToken>): MutableIterable<ErroredFcmToken> {
        return dao.saveAll(list)
    }

    override fun findAll(): Iterable<ErroredFcmToken> {
        return dao.findAll()
    }

    override fun delete(token: ErroredFcmToken) {
        return dao.deleteById(token.id)
    }
}