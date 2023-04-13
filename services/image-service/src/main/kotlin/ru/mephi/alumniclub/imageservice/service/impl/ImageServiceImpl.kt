package ru.mephi.alumniclub.imageservice.service.impl

import org.springframework.stereotype.Service
import ru.mephi.alumniclub.imageservice.service.ImageService
import ru.mephi.alumniclub.imageservice.service.ThumbnailatorService
import ru.mephi.alumniclub.shared.dto.photo.ThumbnailRequest

@Service
class ImageServiceImpl(
    private val thumbnailService: ThumbnailatorService,
) : ImageService {
    override fun compressImage(request: ThumbnailRequest) {
        thumbnailService.handle(request)
    }
}