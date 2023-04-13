package ru.mephi.alumniclub.app.model.mapper.preferences

import org.springframework.stereotype.Component
import ru.mephi.alumniclub.app.database.entity.preferences.UserPreferences
import ru.mephi.alumniclub.app.database.entity.user.User
import ru.mephi.alumniclub.app.model.dto.preferences.UserPreferencesDTO

@Component
class PreferencesMapper {
    fun asResponse(entity: UserPreferences): UserPreferencesDTO {
        return UserPreferencesDTO(
            enablePush = entity.enablePush,
            enableEmail = entity.enableEmail
        )
    }

    fun asEntity(request: UserPreferencesDTO, user: User): UserPreferences {
        return UserPreferences(
            user = user,
            enablePush = request.enablePush,
            enableEmail = request.enableEmail
        )
    }

    fun update(preferences: UserPreferences, request: UserPreferencesDTO) {
        preferences.enableEmail = request.enableEmail
        preferences.enablePush = request.enablePush
    }
}
