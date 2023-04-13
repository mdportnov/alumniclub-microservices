package ru.mephi.alumniclub.app.config

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.web.client.RestTemplate

@Configuration
class WebClientConfig {
    @Bean
    fun restTemplate(): RestTemplate {
        return RestTemplate(
            listOf(
                MappingJackson2HttpMessageConverter(ObjectMapper().also { it.findAndRegisterModules() })
            )
        )
    }
}