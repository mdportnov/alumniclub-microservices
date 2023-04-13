package ru.mephi.alumniclub.imageservice.config

import org.springframework.amqp.core.Queue
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import ru.mephi.alumniclub.shared.util.constants.CONTENT_PHOTO_QUEUE
import ru.mephi.alumniclub.shared.util.constants.IMAGE_THUMBNAIL_QUEUE

@Configuration
class RabbitQueueConfig {
    @Bean
    fun broadcastQueue(): Queue {
        return Queue(IMAGE_THUMBNAIL_QUEUE)
    }

    @Bean
    fun photoContentQueue(): Queue {
        return Queue(CONTENT_PHOTO_QUEUE)
    }
}
