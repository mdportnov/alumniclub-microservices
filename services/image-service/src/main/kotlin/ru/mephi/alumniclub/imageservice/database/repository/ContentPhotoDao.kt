package ru.mephi.alumniclub.imageservice.database.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import ru.mephi.alumniclub.imageservice.database.entity.ContentPhoto
import java.util.*

@Repository
interface ContentPhotoDao : CrudRepository<ContentPhoto, Long> {

    fun findByPhotoPath(photoPath: String): ContentPhoto?

    fun deleteByContentIdAndPhotoPathNotIn(contentId: UUID, photoPath: List<String>)

    fun findByContentIdAndPhotoPathNotIn(contentId: UUID, photoPath: List<String>): List<ContentPhoto>
}