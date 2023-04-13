package ru.mephi.alumniclub.imageservice.database.entity.publication

import org.hibernate.annotations.Type
import java.time.LocalDateTime
import java.util.*
import javax.persistence.*

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
abstract class AbstractPublication(
    @Column(nullable = false)
    var title: String,

    @Lob
    @Column(nullable = false)
    var content: String,

    @Column
    var photoPath: String? = null,
) {
    constructor() : this("", "", null)

    @Id
    @Type(type = "uuid-char")
    val id: UUID = UUID.randomUUID()

    @Column(name = "createdAt", nullable = false)
    var createdAt: LocalDateTime = LocalDateTime.now()
}