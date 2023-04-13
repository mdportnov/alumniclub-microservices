package ru.mephi.alumniclub.imageservice.service.impl

import org.springframework.stereotype.Service
import ru.mephi.alumniclub.imageservice.database.repository.AbstractPublicationDao
import ru.mephi.alumniclub.imageservice.database.repository.ContentPhotoDao
import ru.mephi.alumniclub.imageservice.service.PhotoContentHandler
import ru.mephi.alumniclub.imageservice.util.parseContentPhotoPaths
import ru.mephi.alumniclub.shared.dto.photo.ContentPhotoRequest
import javax.transaction.Transactional

@Service
class PhotoContentHandlerImpl(
    private val storageManager: StorageFileManager,
    private val contentPhotoDao: ContentPhotoDao,
    private val abstractPublicationDao: AbstractPublicationDao,
) : PhotoContentHandler {

    private fun findByPhotoPath(photoPath: String) =
        contentPhotoDao.findByPhotoPath(photoPath)

    @Transactional
    override fun handle(request: ContentPhotoRequest) {
        // TODO hibernate.InstantiationException: No default constructor for entity:  : ru.mephi.alumniclub.imageservice.database.entity.publication.Publication
        val content = abstractPublicationDao.findById(request.id)
        if (!content.isPresent) return
        val photoPaths =
            request.content.parseContentPhotoPaths().map { storageManager.getShortImagePath(it) }.toMutableList()
        photoPaths.forEach { path ->
            val contentPhoto = findByPhotoPath(path) ?: return@forEach
            contentPhoto.content = content.get()
            contentPhotoDao.save(contentPhoto)
        }
        if (photoPaths.isEmpty()) photoPaths.add("")
        contentPhotoDao.deleteByContentIdAndPhotoPathNotIn(request.id, photoPaths)
    }
}