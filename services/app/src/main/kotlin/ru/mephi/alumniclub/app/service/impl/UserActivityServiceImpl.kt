package ru.mephi.alumniclub.app.service.impl

import org.springframework.stereotype.Service
import ru.mephi.alumniclub.app.database.repository.activity.UserActivityDao
import ru.mephi.alumniclub.app.service.UserActivityService

@Service
class UserActivityServiceImpl(
    private val userActivityDao: UserActivityDao
) : UserActivityService {
    override fun handle(principal: Long) {
        try {
            userActivityDao.update(principal)
        } catch (e: Exception) {
            return
        }
    }
}
