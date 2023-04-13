package ru.mephi.alumniclub.app.database.repository.content

import org.springframework.stereotype.Repository
import ru.mephi.alumniclub.app.database.entity.content.ContentPhoto
import ru.mephi.alumniclub.shared.database.repository.AbstractRepository

@Repository
interface ContentPhotoDao : AbstractRepository<ContentPhoto> {
    fun findByPhotoPath(photoPath: String): ContentPhoto?
}