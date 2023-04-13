package ru.mephi.alumniclub.app.config

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.amqp.core.AmqpAdmin
import org.springframework.amqp.rabbit.annotation.EnableRabbit
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory
import org.springframework.amqp.rabbit.connection.ConnectionFactory
import org.springframework.amqp.rabbit.core.RabbitAdmin
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.amqp.rabbit.listener.ConditionalRejectingErrorHandler
import org.springframework.amqp.rabbit.listener.ConditionalRejectingErrorHandler.DefaultExceptionStrategy
import org.springframework.amqp.rabbit.support.ListenerExecutionFailedException
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.util.ErrorHandler
import ru.mephi.alumniclub.app.config.properties.RabbitProperties
import ru.mephi.alumniclub.shared.logging.AlumniLogger

@Configuration
@EnableRabbit
class RabbitMQConfig(
    private val properties: RabbitProperties,
    private val logger: AlumniLogger
) {
    @Bean
    fun jsonMessageConverter(): Jackson2JsonMessageConverter {
        return Jackson2JsonMessageConverter(ObjectMapper().also { it.findAndRegisterModules() })
    }

    @Bean
    fun connectionFactory(): ConnectionFactory {
        val connectionFactory = CachingConnectionFactory()
        connectionFactory.apply {
            host = properties.host
            username = properties.username
            port = properties.port
            setPassword(properties.password)
        }
        return connectionFactory
    }

    @Bean
    fun rabbitTemplate(connectionFactory: ConnectionFactory): RabbitTemplate {
        val rabbitTemplate = RabbitTemplate(connectionFactory)
        rabbitTemplate.messageConverter = jsonMessageConverter()
        rabbitTemplate.setReplyTimeout(properties.replyTimeout)
        rabbitTemplate.setUseDirectReplyToContainer(false)
        return rabbitTemplate
    }

    @Bean
    fun amqpAdmin(): AmqpAdmin {
        return RabbitAdmin(connectionFactory())
    }

    @Bean
    fun rabbitErrorHandler(): ErrorHandler {
        return ConditionalRejectingErrorHandler(RabbitFatalExceptionStrategy(logger))
    }

    class RabbitFatalExceptionStrategy(
        private val errorLogger: AlumniLogger
    ) : DefaultExceptionStrategy() {
        override fun isUserCauseFatal(cause: Throwable): Boolean {
            errorLogger.error(cause.localizedMessage, cause)
            return super.isUserCauseFatal(cause)
        }

        override fun isFatal(throwable: Throwable): Boolean {
            if (throwable is ListenerExecutionFailedException) {
                errorLogger.error(
                    "Failed to process inbound message from queue "
                            + throwable.failedMessage.messageProperties.consumerQueue
                            + "; failed message: " + throwable.failedMessage, throwable
                )
            }
            return super.isFatal(throwable)
        }
    }
}