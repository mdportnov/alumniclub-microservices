package ru.mephi.alumniclub.shared.database.entity

import org.hibernate.annotations.CreationTimestamp
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.MappedSuperclass

@MappedSuperclass
abstract class AbstractCreatedAtEntity : AbstractEntity() {
    @CreationTimestamp
    @Column(nullable = false)
    lateinit var createdAt: LocalDateTime
}
