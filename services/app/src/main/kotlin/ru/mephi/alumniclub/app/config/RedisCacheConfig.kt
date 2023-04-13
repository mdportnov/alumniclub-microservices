package ru.mephi.alumniclub.app.config

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration
import org.springframework.cache.Cache
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.context.annotation.Primary
import org.springframework.data.redis.cache.RedisCacheConfiguration
import org.springframework.data.redis.cache.RedisCacheManager
import org.springframework.data.redis.cache.RedisCacheWriter
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair
import org.springframework.util.Assert
import ru.mephi.alumniclub.app.config.properties.RedisProperties
import ru.mephi.alumniclub.shared.util.constants.LIKES_COUNT_CACHE
import java.time.Duration
import java.util.concurrent.Callable

@ConditionalOnProperty(prefix = "spring", name = ["cache.type"], havingValue = "redis")
@Configuration
@EnableCaching
@Import(RedisAutoConfiguration::class)
class RedisCacheConfig(
    private val properties: RedisProperties
) {
    @Bean
    fun customRedisCacheConfig(): RedisCacheConfiguration {
        return RedisCacheConfiguration.defaultCacheConfig().disableCachingNullValues()
            .serializeValuesWith(SerializationPair.fromSerializer(GenericJackson2JsonRedisSerializer()))
    }

    @Bean
    @Primary
    fun redisCacheManager(
        config: RedisCacheConfiguration, connectionFactory: LettuceConnectionFactory
    ): RedisCacheManager {
        val map = HashMap<String, RedisCacheConfiguration>()
        map[LIKES_COUNT_CACHE] = config.entryTtl(Duration.ofSeconds(properties.cacheTTL))

        val cacheWriter = RedisCacheWriter.nonLockingRedisCacheWriter(connectionFactory)
        return CustomRedisCacheManager(cacheWriter, config, map)
    }

    class CustomRedisCacheManager(
        cacheWriter: RedisCacheWriter,
        defaultCacheConfiguration: RedisCacheConfiguration,
        initialCacheConfigurations: Map<String, RedisCacheConfiguration>
    ) : RedisCacheManager(cacheWriter, defaultCacheConfiguration, initialCacheConfigurations) {

        override fun getCache(name: String): Cache {
            return RedisCacheWrapper(super.getCache(name))
        }

        class RedisCacheWrapper(redisCache: Cache?) : Cache {
            private val delegate: Cache?

            init {
                Assert.notNull(redisCache, "delegate cache must not be null")
                delegate = redisCache
            }

            override fun getName(): String {
                return try {
                    delegate!!.name
                } catch (e: Exception) {
                    handleException<String>(e)!!
                }
            }

            override fun getNativeCache(): Any {
                return try {
                    delegate!!.nativeCache
                } catch (e: Exception) {
                    handleException<Any>(e)!!
                }
            }

            override fun get(key: Any): Cache.ValueWrapper? {
                return try {
                    delegate!![key]
                } catch (e: Exception) {
                    handleException<Cache.ValueWrapper>(e)
                }
            }

            override fun <T> get(o: Any, aClass: Class<T>?): T? {
                return try {
                    delegate!!.get(o, aClass)
                } catch (e: Exception) {
                    handleException<T>(e)
                }
            }

            override fun <T> get(o: Any, callable: Callable<T>): T? {
                return try {
                    delegate!!.get(o, callable)
                } catch (e: Exception) {
                    handleException<T>(e)
                }
            }

            override fun put(key: Any, value: Any?) {
                try {
                    delegate!!.put(key, value)
                } catch (e: Exception) {
                    handleException<Any>(e)
                }
            }

            override fun putIfAbsent(o: Any, o1: Any?): Cache.ValueWrapper? {
                return try {
                    delegate!!.putIfAbsent(o, o1)
                } catch (e: Exception) {
                    handleException<Cache.ValueWrapper>(e)
                }
            }

            override fun evict(o: Any) {
                try {
                    delegate!!.evict(o)
                } catch (e: Exception) {
                    handleException<Any>(e)
                }
            }

            override fun clear() {
                try {
                    delegate!!.clear()
                } catch (e: Exception) {
                    handleException<Any>(e)
                }
            }

            private fun <T> handleException(e: Exception): T? {
                e.printStackTrace()
                return null
            }
        }
    }
}

