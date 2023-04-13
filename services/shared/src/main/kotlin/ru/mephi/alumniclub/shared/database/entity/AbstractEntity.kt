package ru.mephi.alumniclub.shared.database.entity

import java.io.Serializable
import javax.persistence.*

@MappedSuperclass
abstract class AbstractEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    open var id: Long = 0
) : Serializable
