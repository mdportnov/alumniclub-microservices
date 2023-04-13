package ru.mephi.alumniclub.app.database.entity.feed

import ru.mephi.alumniclub.shared.database.entity.AbstractEntity
import ru.mephi.alumniclub.shared.util.constants.smallLength
import javax.persistence.*

@Entity
@Table(name = "Feed")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.STRING)
abstract class AbstractFeed(
    @Column(length = smallLength)
    var name: String
) : AbstractEntity()
