package ru.mephi.alumniclub.app.service.helpers

import org.springframework.data.jpa.repository.Modifying
import org.springframework.stereotype.Service
import ru.mephi.alumniclub.app.database.entity.user.User
import ru.mephi.alumniclub.app.database.repository.user.UserDao
import ru.mephi.alumniclub.app.model.dto.FeatureToggleUpdateRequest
import ru.mephi.alumniclub.app.model.dto.user.DegreeDTO
import ru.mephi.alumniclub.app.model.dto.user.request.BioRequest
import ru.mephi.alumniclub.app.model.dto.user.request.RegistrationRequest
import ru.mephi.alumniclub.app.model.enumeration.user.DegreeType
import ru.mephi.alumniclub.app.model.enumeration.user.Role
import ru.mephi.alumniclub.app.service.AuthService
import ru.mephi.alumniclub.app.service.FeatureToggleService
import ru.mephi.alumniclub.app.service.UserService
import java.time.LocalDate
import javax.transaction.Transactional

@Service
class UserHelper(
    private val userDao: UserDao,

    private val authService: AuthService,
    private val userService: UserService,
    private val featureToggleService: FeatureToggleService
) {
    val birthday: LocalDate = LocalDate.now().minusYears(19)

    fun toggleVerification(value: Boolean = false) {
        val request = FeatureToggleUpdateRequest("verifyEmail", value)
        featureToggleService.changeFeatureState(request)
    }

    fun degreeDto(vararg degreeTypes: DegreeType) = degreeTypes.map { DegreeDTO(2000, 2004, it, it.name) }

    fun registrationRequest(
        email: String = UserMock.EMAIL.value,
        roles: Set<Role> = setOf(Role.WORKER),
        degrees: List<DegreeDTO> = degreeDto(DegreeType.WORKER),
        bio: BioRequest
    ) = RegistrationRequest(
        password = UserMock.PASSWORD.value, email = email,
        name = "Name", surname = "Surname", gender = true,
        birthday = birthday, roles = roles,
        degrees = degrees, bio = bio
    )

    @Transactional
    fun registerUser(
        email: String = UserMock.EMAIL.value,
        roles: Set<Role> = setOf(Role.WORKER),
        degrees: List<DegreeDTO> = degreeDto(DegreeType.WORKER),
        bio: BioRequest
    ): User {
        val user = authService.register(registrationRequest(email, roles, degrees, bio))
        return userService.findUserEntityById(user.id as Long)
    }

    @Transactional
    fun registerAdmin(): User {
        val user = userService.createUser(registrationRequest(roles = setOf(Role.ADMIN), bio = BioRequest()))
        return userService.findUserEntityById(user.id)
    }

    @Modifying
    fun deleteAll() {
        userDao.deleteAll()
    }
}
