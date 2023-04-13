package ru.mephi.recommendations.config

import org.springframework.amqp.core.Queue
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import ru.mephi.recommendations.utils.PUBLICATIONS_EVENT_QUEUE
import ru.mephi.recommendations.utils.PUBLICATIONS_USERS_FEEDBACK_QUEUE

@Configuration
class RabbitQueueConfig {
    @Bean
    fun publicationEventsQueue(): Queue {
        return Queue(PUBLICATIONS_EVENT_QUEUE)
    }

    @Bean
    fun userFeedBackQueue(): Queue {
        return Queue(PUBLICATIONS_USERS_FEEDBACK_QUEUE)
    }
}