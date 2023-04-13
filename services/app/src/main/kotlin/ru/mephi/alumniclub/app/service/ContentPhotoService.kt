package ru.mephi.alumniclub.app.service

import ru.mephi.alumniclub.app.model.dto.content.ContentPhotoResponse
import ru.mephi.alumniclub.shared.dto.photo.ContentPhotoRequest
import javax.servlet.http.Part

interface ContentPhotoService {
    fun queueContent(request: ContentPhotoRequest)
    fun contentExists(photoPath: String): Boolean
    fun createContentPhoto(file: Part): ContentPhotoResponse
    fun deleteContentPhoto(photoPath: String)
}