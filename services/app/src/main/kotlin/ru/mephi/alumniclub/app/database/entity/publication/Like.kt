package ru.mephi.alumniclub.app.database.entity.publication

import org.hibernate.annotations.Type
import ru.mephi.alumniclub.app.database.entity.user.User
import java.util.*
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Index
import javax.persistence.Table

@Entity
@Table(name = "Likes", indexes = [Index(columnList = "publicationId")])
class Like(
    user: User,

    @Type(type = "uuid-char")
    @Column(nullable = false)
    val publicationId: UUID
) : AbstractReaction(user)
