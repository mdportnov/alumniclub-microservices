package ru.mephi.alumniclub.imageservice.listeners

import ru.mephi.alumniclub.shared.dto.photo.ContentPhotoRequest

interface ContentPhotoListener {
    fun processPhotoContent(request: ContentPhotoRequest)
}