package ru.mephi.alumniclub.imageservice.service

import ru.mephi.alumniclub.shared.dto.photo.ContentPhotoRequest

interface PhotoContentHandler {
    fun handle(request: ContentPhotoRequest)
}