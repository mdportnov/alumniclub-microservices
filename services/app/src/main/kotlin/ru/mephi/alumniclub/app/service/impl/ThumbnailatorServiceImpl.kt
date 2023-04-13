package ru.mephi.alumniclub.app.service.impl

import org.springframework.stereotype.Service
import ru.mephi.alumniclub.app.service.RabbitService
import ru.mephi.alumniclub.app.service.ThumbnailatorService
import ru.mephi.alumniclub.shared.dto.photo.ThumbnailRequest
import ru.mephi.alumniclub.shared.logging.AlumniLogger
import ru.mephi.alumniclub.shared.util.enums.RabbitMessageType

@Service
class ThumbnailatorServiceImpl(
    private val rabbitService: RabbitService,
    private val logger: AlumniLogger
) : ThumbnailatorService {

    override fun sendToQueue(request: ThumbnailRequest) {
        logger.info("App Service send \"${request}\" to queue for compressing")
        rabbitService.sendMessage(request, RabbitMessageType.IMAGE_THUMBNAIL_QUEUE)
    }
}