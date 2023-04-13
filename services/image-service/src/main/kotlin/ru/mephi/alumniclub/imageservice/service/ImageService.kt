package ru.mephi.alumniclub.imageservice.service

import ru.mephi.alumniclub.shared.dto.photo.ThumbnailRequest

interface ImageService {
    fun compressImage(request: ThumbnailRequest)
}