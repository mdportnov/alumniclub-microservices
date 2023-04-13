package ru.mephi.alumniclub.app.model.dto.project

interface IdColorProjection {
    val id: Long?
    val color: String?

    companion object {
        fun empty() = object : IdColorProjection {
            override val id = null
            override val color = null
        }
    }

    operator fun component1() = id
    operator fun component2() = color
}