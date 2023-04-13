package ru.mephi.alumniclub.app.database.entity.user

import org.springframework.data.jpa.repository.Modifying
import ru.mephi.alumniclub.app.database.entity.broadcast.Broadcast
import ru.mephi.alumniclub.app.database.entity.community.UserCommunity
import ru.mephi.alumniclub.app.database.entity.publication.Event
import ru.mephi.alumniclub.app.database.entity.publication.Publication
import ru.mephi.alumniclub.app.database.entity.survey.Survey
import ru.mephi.alumniclub.shared.dto.security.PermissionsDTO
import ru.mephi.alumniclub.app.model.enumeration.user.Role
import ru.mephi.alumniclub.app.model.mapper.permission.PermissionsConverter
import ru.mephi.alumniclub.shared.database.entity.AbstractCreatedAtEntity
import ru.mephi.alumniclub.shared.util.constants.*
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import javax.persistence.*

@Entity
@Table(name = "User")
final class User(
    @Column(nullable = false, length = mediumLength, unique = true)
    var email: String,

    @Column(length = phoneMaxLength)
    var phone: String? = null,

    @Column(length = smallLength)
    var vk: String? = null,

    @Column(length = smallLength)
    var tg: String? = null,

    @Column(nullable = false, length = extraSmallLength)
    var name: String,

    @Column(nullable = false, length = extraSmallLength)
    var surname: String,

    @Column(nullable = true, length = extraSmallLength)
    var patronymic: String? = null,

    @Column(nullable = false)
    var gender: Boolean,

    @Column(nullable = false)
    var birthday: LocalDate,

    @Column(length = photoPathLength)
    var photoPath: String? = null,

    @Column(nullable = false)
    var banned: Boolean = false
) : AbstractCreatedAtEntity() {

    @Column(nullable = false)
    var lyceum: Boolean = false
        private set

    @Column(nullable = false)
    var student: Boolean = false
        private set

    @Column(nullable = false)
    var alumnus: Boolean = false
        private set

    @Column(nullable = false)
    var worker: Boolean = false
        private set

    @Column(nullable = false)
    var mentor: Boolean = false
        private set

    @Column(nullable = false)
    var admin: Boolean = false
        private set

    @Column(nullable = false)
    var moderator: Boolean = false
        private set

    @Column(nullable = false, length = 60)
    var hash: String = ""
        set(value) {
            field = value
            lastPasswordUpdate = LocalDateTime.now()
        }

    @Column(nullable = false)
    var lastPasswordUpdate: LocalDateTime = LocalDateTime.now()

    @Convert(converter = PermissionsConverter::class)
    @Column(length = extraLargeLength, columnDefinition = "json")
    var permissions: PermissionsDTO = PermissionsDTO()

    @Transient
    var age: Long = 0

    @Transient
    lateinit var fullName: String

    @Transient
    lateinit var roles: Set<Role>

    @Modifying
    fun updateRoles(roles: Set<Role>) {
        this.roles = roles
        lyceum = Role.LYCEUM in roles
        student = Role.STUDENT in roles
        alumnus = Role.ALUMNUS in roles
        worker = Role.WORKER in roles
        mentor = Role.MENTOR in roles
        moderator = Role.MODERATOR in roles
        admin = Role.ADMIN in roles
    }

    @PostLoad
    @PostPersist
    fun initValues() {
        age = birthday.until(LocalDate.now(), ChronoUnit.YEARS)
        fullName = listOfNotNull(surname, name, patronymic).joinToString(" ")
        roles = setOfNotNull(
            if (lyceum) Role.LYCEUM else null,
            if (student) Role.STUDENT else null,
            if (alumnus) Role.ALUMNUS else null,
            if (worker) Role.WORKER else null,
            if (mentor) Role.MENTOR else null,
            if (moderator) Role.MODERATOR else null,
            if (admin) Role.ADMIN else null,
        )
    }

    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL])
    val communities = mutableListOf<UserCommunity>()

    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY)
    val publications = mutableListOf<Publication>()

    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY)
    val events = mutableListOf<Event>()

    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY)
    val broadcasts = mutableListOf<Broadcast>()

    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY)
    val surveys = mutableListOf<Survey>()

    @PreRemove
    @Modifying
    fun destroyRelationships() {
        publications.forEach { it.author = null }
        events.forEach { it.author = null }
        surveys.forEach { it.author = null }
        broadcasts.forEach { it.author = null }
    }
}
