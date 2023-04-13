package ru.mephi.alumniclub.imageservice.listeners.impl

import org.springframework.amqp.rabbit.annotation.EnableRabbit
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Component
import ru.mephi.alumniclub.imageservice.listeners.ContentPhotoListener
import ru.mephi.alumniclub.imageservice.service.PhotoContentHandler
import ru.mephi.alumniclub.shared.dto.photo.ContentPhotoRequest
import ru.mephi.alumniclub.shared.util.constants.CONTENT_PHOTO_QUEUE

@Component
@EnableRabbit
class ContentPhotoListenerImpl(
    private val handler: PhotoContentHandler,
) : ContentPhotoListener {

    @RabbitListener(queues = [CONTENT_PHOTO_QUEUE])
    override fun processPhotoContent(request: ContentPhotoRequest) {
        try {
            handler.handle(request)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}