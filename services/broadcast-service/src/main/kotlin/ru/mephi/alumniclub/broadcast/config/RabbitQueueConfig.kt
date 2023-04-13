package ru.mephi.alumniclub.broadcast.config

import org.springframework.amqp.core.Queue
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import ru.mephi.alumniclub.shared.util.constants.MAIL_BROADCAST
import ru.mephi.alumniclub.shared.util.constants.MAIL_PUBLICATION_CONTENT
import ru.mephi.alumniclub.shared.util.constants.MAIL_RESET_PASSWORD
import ru.mephi.alumniclub.shared.util.constants.MAIL_VERIFY_EMAIL

@Configuration
class RabbitQueueConfig {
    @Bean
    fun publicationContentQueue(): Queue {
        return Queue(MAIL_PUBLICATION_CONTENT)
    }

    @Bean
    fun verifyEmailQueue(): Queue {
        return Queue(MAIL_VERIFY_EMAIL)
    }

    @Bean
    fun resetPasswordQueue(): Queue {
        return Queue(MAIL_RESET_PASSWORD)
    }

    @Bean
    fun broadcastQueue(): Queue {
        return Queue(MAIL_BROADCAST)
    }
}