package ru.mephi.alumniclub.app.config

import com.hazelcast.core.HazelcastInstance
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import ru.mephi.alumniclub.app.security.IRateLimiter
import ru.mephi.alumniclub.app.security.RateLimiterProdImpl
import ru.mephi.alumniclub.app.security.RateLimiterTestImpl

@Configuration
class RateLimiterConfig(
    @Qualifier("HzInstance")
    private val hzInstance: HazelcastInstance
) {
    @Profile("prod")
    @Bean("RateLimiter")
    fun prodRateLimiter(): IRateLimiter {
        return RateLimiterProdImpl(hzInstance)
    }

    @Profile("dev | test | local")
    @Bean("RateLimiter")
    fun devRateLimiter(): IRateLimiter {
        return RateLimiterTestImpl(hzInstance)
    }
}