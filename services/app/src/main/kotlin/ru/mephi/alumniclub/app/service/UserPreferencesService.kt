package ru.mephi.alumniclub.app.service

import io.swagger.v3.oas.annotations.Parameter
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import ru.mephi.alumniclub.app.database.entity.preferences.UserPreferences
import ru.mephi.alumniclub.app.database.entity.user.User
import ru.mephi.alumniclub.app.model.dto.preferences.UserPreferencesDTO

interface UserPreferencesService {
    fun setDefaultPreferences(user: User): UserPreferences
    fun updatePreferences(
        @Parameter(hidden = true) userId: Long,
        @RequestBody request: UserPreferencesDTO
    ): UserPreferencesDTO
    fun updatePreferencesByEmailToken(token: String): UserPreferencesDTO
    fun findEntityByUserId(userId: Long): UserPreferences
    fun findAllUsersIdsWithEnablePush(ignorePreferences: Boolean = false): List<Long>
    fun getPreferencesById(@PathVariable("id") userId: Long): UserPreferencesDTO
    fun getUnsubscribeFromMailLink(): String
}
