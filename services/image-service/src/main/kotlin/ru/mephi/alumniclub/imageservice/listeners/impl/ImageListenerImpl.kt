package ru.mephi.alumniclub.imageservice.listeners.impl

import org.springframework.amqp.rabbit.annotation.EnableRabbit
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Component
import ru.mephi.alumniclub.imageservice.listeners.ImageListener
import ru.mephi.alumniclub.imageservice.service.ImageService
import ru.mephi.alumniclub.shared.dto.photo.ThumbnailRequest
import ru.mephi.alumniclub.shared.util.constants.IMAGE_THUMBNAIL_QUEUE

@Component
@EnableRabbit
class ImageListenerImpl(
    val imageService: ImageService,
) : ImageListener {

    @RabbitListener(queues = [IMAGE_THUMBNAIL_QUEUE])
    override fun compressImage(request: ThumbnailRequest) {
        try {
            imageService.compressImage(request)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
