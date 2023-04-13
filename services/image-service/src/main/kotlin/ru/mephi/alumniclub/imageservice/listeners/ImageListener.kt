package ru.mephi.alumniclub.imageservice.listeners

import ru.mephi.alumniclub.shared.dto.photo.ThumbnailRequest


interface ImageListener {
    fun compressImage(request: ThumbnailRequest)
}
