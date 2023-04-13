package ru.mephi.alumniclub.app.database.repository.preferences

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import ru.mephi.alumniclub.app.database.entity.preferences.UserPreferences

@Repository
interface UserPreferencesDao : CrudRepository<UserPreferences, Long> {

    fun findByUserId(userId: Long): UserPreferences?

    @Query("SELECT up.userId FROM UserPreferences up WHERE (up.enableEmail or :ignore)", nativeQuery = true)
    fun findAllUsersIdsWithEnableEmail(@Param("ignore") ignorePreferences: Boolean): List<Long>

    @Query("SELECT up.userId FROM UserPreferences up WHERE (up.enablePush or :ignore)", nativeQuery = true)
    fun findAllUsersIdsWithEnablePush(@Param("ignore") ignorePreferences: Boolean): List<Long>
}