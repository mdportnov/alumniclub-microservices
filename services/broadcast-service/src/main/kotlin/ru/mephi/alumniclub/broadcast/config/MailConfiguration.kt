package ru.mephi.alumniclub.broadcast.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.JavaMailSenderImpl
import ru.mephi.alumniclub.broadcast.config.properties.MailProperties

@Configuration
class MailConfiguration(
    private val properties: MailProperties
) {
    @Bean
    fun mailSender(): JavaMailSender {
        val mailSender = JavaMailSenderImpl().apply {
            host = properties.host
            port = properties.port
            username = properties.username
            password = properties.password
            defaultEncoding = Charsets.UTF_8.name()
        }
        val props = mailSender.javaMailProperties
        props["mail.transport.protocol"] = "smtp"
        props["mail.smtp.auth"] = properties.properties["mail.smtp.auth"]
        props["mail.smtp.starttls.enable"] = properties.properties["mail.smtp.starttls.enable"]
        return mailSender
    }
}