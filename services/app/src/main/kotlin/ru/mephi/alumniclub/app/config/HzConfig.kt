package ru.mephi.alumniclub.app.config

import com.hazelcast.config.Config
import com.hazelcast.core.Hazelcast
import com.hazelcast.core.HazelcastInstance
import com.hazelcast.spring.cache.HazelcastCacheManager
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.cache.CacheManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class HzConfig {
    @Bean
    fun hazelCastConfig(): Config {
        val config = Config().apply {
            instanceName = "HzInstance"
        }
        return config
    }

    @Bean("HzInstance")
    fun getInstance(): HazelcastInstance {
        return Hazelcast.getOrCreateHazelcastInstance(hazelCastConfig())
    }

    @Bean
    fun cacheManager(@Qualifier("HzInstance") hazelcastInstance: HazelcastInstance): CacheManager {
        return HazelcastCacheManager(hazelcastInstance)
    }
}