package ru.mephi.alumniclub.app.service

import ru.mephi.alumniclub.shared.dto.photo.ThumbnailRequest

interface ThumbnailatorService {
    fun sendToQueue(request: ThumbnailRequest)
}