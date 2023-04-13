package ru.mephi.alumniclub.app.database.entity.community

import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import ru.mephi.alumniclub.app.database.entity.user.User
import ru.mephi.alumniclub.shared.database.entity.AbstractCreatedAtEntity
import javax.persistence.*


@Entity
@Table(name = "UsersCommunities")
class UserCommunity(
    @OnDelete(action = OnDeleteAction.CASCADE)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    val user: User,

    @OnDelete(action = OnDeleteAction.CASCADE)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "communityId")
    val community: Community
) : AbstractCreatedAtEntity()
