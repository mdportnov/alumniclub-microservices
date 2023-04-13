package ru.mephi.alumniclub.app.database.entity.community

import org.springframework.data.jpa.repository.Modifying
import ru.mephi.alumniclub.app.database.entity.project.AbstractProject
import ru.mephi.alumniclub.app.database.entity.user.User
import ru.mephi.alumniclub.shared.database.entity.AbstractCreatedAtEntity
import ru.mephi.alumniclub.shared.util.constants.photoPathLength
import ru.mephi.alumniclub.shared.util.constants.smallLength
import javax.persistence.*


@Entity
@Table(name = "Community")
class Community(
    @Column(nullable = false, length = smallLength)
    var name: String,

    @Column(nullable = false)
    var role: Boolean = false,

    @Column(nullable = false)
    var hidden: Boolean = false,

    @Column(length = photoPathLength)
    var photoPath: String? = null
) : AbstractCreatedAtEntity() {

    @get:Transient
    val membersCount
        get() = users.filter { !it.banned }.size

    @ManyToMany(cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    @JoinTable(
        joinColumns = [JoinColumn(name = "communityId")],
        inverseJoinColumns = [JoinColumn(name = "userId")],
        name = "UsersCommunities"
    )
    var users: MutableList<User> = mutableListOf()

    @OneToOne(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    @JoinColumn(name = "projectId")
    var project: AbstractProject? = null

    @Modifying
    @PreRemove
    fun deleteUsersCommunities() {
        users.removeAll(users)
    }
}