package ru.mephi.alumniclub.app.service.impl

import org.springframework.data.jpa.repository.Modifying
import org.springframework.stereotype.Service
import ru.mephi.alumniclub.app.config.properties.ServerUrlsProperties
import ru.mephi.alumniclub.app.database.entity.preferences.UserPreferences
import ru.mephi.alumniclub.app.database.entity.user.User
import ru.mephi.alumniclub.app.database.repository.preferences.UserPreferencesDao
import ru.mephi.alumniclub.app.model.dto.preferences.UserPreferencesDTO
import ru.mephi.alumniclub.app.model.exception.common.ResourceNotFoundException
import ru.mephi.alumniclub.app.model.mapper.preferences.PreferencesMapper
import ru.mephi.alumniclub.app.service.UserPreferencesService
import ru.mephi.alumniclub.app.service.UserService
import ru.mephi.alumniclub.app.service.impl.auth.JwtManager
import ru.mephi.alumniclub.shared.util.extension.userId
import ru.mephi.alumniclub.shared.util.response.ResponseManager
import javax.transaction.Transactional

@Service
class UserPreferencesServiceImpl(
    private val dao: UserPreferencesDao,
    private val mapper: PreferencesMapper,
    private val userService: UserService,
    private val jwtManager: JwtManager,
    private val serverUrlsProperties: ServerUrlsProperties
) : ResponseManager(), UserPreferencesService {

    @Transactional
    override fun setDefaultPreferences(user: User): UserPreferences {
        return dao.save(UserPreferences(user))
    }

    @Transactional
    override fun updatePreferencesByEmailToken(token: String): UserPreferencesDTO {
        val userId = jwtManager.parseToken(token).userId
        val preferences = findEntityByUserId(userId)
        preferences.enableEmail = false
        return mapper.asResponse(preferences)
    }

    @Modifying
    @Transactional
    override fun updatePreferences(userId: Long, request: UserPreferencesDTO): UserPreferencesDTO {
        val preferences = dao.findByUserId(userId) ?: throw ResourceNotFoundException(UserPreferences::class.java)
        mapper.update(preferences, request)
        return mapper.asResponse(preferences)
    }

    override fun findEntityByUserId(userId: Long): UserPreferences {
        return dao.findById(userId).orElseGet {
            setDefaultPreferences(userService.findUserEntityById(userId))
        }
    }

    override fun findAllUsersIdsWithEnablePush(ignorePreferences: Boolean): List<Long> {
        return dao.findAllUsersIdsWithEnablePush(ignorePreferences)
    }

    override fun getPreferencesById(userId: Long): UserPreferencesDTO {
        val entity = findEntityByUserId(userId)
        return mapper.asResponse(entity)
    }

    override fun getUnsubscribeFromMailLink(): String {
        return "${serverUrlsProperties.baseUrl}/api/v1/public/preferences/turnOff"
    }
}